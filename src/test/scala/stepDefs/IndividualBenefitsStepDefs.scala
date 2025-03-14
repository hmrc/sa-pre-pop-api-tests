/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stepDefs

import io.cucumber.scala.{EN, ScalaDsl}
import models.{IndividualBenefitsEmployment, IndividualBenefitsResponse}
import org.scalatest.matchers.should.*
import pages.CreateTestUser.createTestUserBenefits
import pages.GetIndividualBenefits.*
import play.api.libs.json.{JsString, JsValue}

class IndividualBenefitsStepDefs extends ScalaDsl with EN with Matchers {

  val maximumModel: IndividualBenefitsResponse = IndividualBenefitsResponse(
    employments = List(
      IndividualBenefitsEmployment(
        employerPayeReference = "123/AB456",
        companyCarsAndVansBenefit = Some(44.0),
        fuelForCompanyCarsAndVansBenefit = Some(99.0),
        privateMedicalDentalInsurance = Some(64.0),
        vouchersCreditCardsExcessMileageAllowance = Some(80.0),
        goodsEtcProvidedByEmployer = Some(125.0),
        accommodationProvidedByEmployer = Some(375.0),
        otherBenefits = Some(16.0),
        expensesPaymentsReceived = Some(415.0)
      ),
      IndividualBenefitsEmployment(
        employerPayeReference = "456/AB456",
        companyCarsAndVansBenefit = Some(50.0),
        fuelForCompanyCarsAndVansBenefit = Some(249.0),
        privateMedicalDentalInsurance = Some(75.0),
        vouchersCreditCardsExcessMileageAllowance = Some(23.0),
        goodsEtcProvidedByEmployer = Some(250.0),
        accommodationProvidedByEmployer = Some(275.0),
        otherBenefits = Some(87.0),
        expensesPaymentsReceived = Some(265.0)
      )
    )
  )

  val minimumModel: IndividualBenefitsResponse =
    IndividualBenefitsResponse(
      employments = List(
        IndividualBenefitsEmployment(
          employerPayeReference = "123/AB456"
        )
      )
    )

  Given("""^I have generated a test user for individual-benefits with the UTR: (.*) for: (.*) with scenario: (.*)$""") {
    (utr: String, taxYear: String, scenario: String) =>
      createTestUserBenefits(utr, taxYear, scenario, "benefits")
  }

  Then(
    """^I should get the (.*) success status when calling individual-benefits with UTR: (.*) and tax year: (.*) for scenario: (.*)$"""
  ) { (statusCode: Int, utr: String, taxYear: String, scenario: String) =>
    val response = getIndividualBenefitsResponse(utr, taxYear)(headers: _*)
    response.status shouldBe statusCode

    val jsonResponseData = response.data

    val individualBenefitsResponse: IndividualBenefitsResponse = jsonResponseData.as[IndividualBenefitsResponse]

    scenario match {
      case "HAPPY_PATH_1" => individualBenefitsResponse shouldBe maximumModel
      case "HAPPY_PATH_2" => individualBenefitsResponse shouldBe minimumModel
      case s              =>
        fail(s"[IndividualBenefitsStepDefs][scenarioCheck] scenario $s does not match or exist")
    }
  }

  Then(
    """^I should get the statusCode: (.*) responseCode: (.*) responseMessage: (.*) when calling individual-benefits with UTR: (.*) and tax year: (.*)$$"""
  ) { (statusCode: Int, responseCode: String, responseMessage: String, utr: String, taxYear: String) =>
    val NOT_ACCEPTABLE = 406
    val response       =
      statusCode match {
        case NOT_ACCEPTABLE => getIndividualBenefitsResponse(utr, taxYear)()
        case _              => getIndividualBenefitsResponse(utr, taxYear)(headers: _*)
      }

    val jsonData: JsValue = response.data

    val code    = (jsonData \ "code").as[JsString].value
    val message = (jsonData \ "message").as[JsString].value

    responseCode    shouldBe code
    message         shouldBe responseMessage
    response.status shouldBe statusCode
  }
}
