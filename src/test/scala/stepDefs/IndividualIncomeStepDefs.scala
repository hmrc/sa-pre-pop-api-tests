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
import models.{Employment, IndividualIncomeResponse, PensionsAnnuitiesAndOtherStateBenefits, Response}
import org.scalatest.matchers.should.Matchers
import pages.CreateTestUser.createTestUserBenefits
import pages.GetIndividualIncome.*
import pages.LocalBearerGenerator
import play.api.libs.json.{JsString, JsValue}

class IndividualIncomeStepDefs extends ScalaDsl with EN with Matchers {

  val maximumModel: IndividualIncomeResponse =
    IndividualIncomeResponse(
      pensionsAnnuitiesAndOtherStateBenefits = PensionsAnnuitiesAndOtherStateBenefits(
        otherPensionsAndRetirementAnnuities = Some(36.50),
        incapacityBenefit = Some(980.45),
        jobseekersAllowance = Some(89.99),
        seissNetPaid = Some(55.55)
      ),
      employments = List(
        Employment(employerPayeReference = "123/AB456", payFromEmployment = 22500.00),
        Employment(employerPayeReference = "456/AB456", payFromEmployment = 8650.00)
      )
    )

  val minimumModel: IndividualIncomeResponse =
    IndividualIncomeResponse(
      pensionsAnnuitiesAndOtherStateBenefits = PensionsAnnuitiesAndOtherStateBenefits(
        otherPensionsAndRetirementAnnuities = None,
        incapacityBenefit = None,
        jobseekersAllowance = None,
        seissNetPaid = None
      ),
      employments = List(
        Employment(employerPayeReference = "123/AB456", payFromEmployment = 22500.00)
      )
    )

  Given(
    """^I have generated a test user for individual-income with the UTR: (.*) for: (.*) with scenario: (.*)$"""
  ) { (utr: String, taxYear: String, scenario: String) =>
    createTestUserBenefits(utr, taxYear, scenario, "income")
  }

  Then(
    """^I should get the (.*) success status when calling individual-income with UTR: (.*) and tax year: (.*) for scenario: (.*)$"""
  ) { (statusCode: Int, utr: String, taxYear: String, scenario: String) =>
    val response: Response                                 = getIndividualIncomeResponse(utr, taxYear)(headers: _*)
    val jsonResponseData: JsValue                          = response.data
    val individualIncomeResponse: IndividualIncomeResponse = jsonResponseData.as[IndividualIncomeResponse]

    statusCode shouldBe response.status

    scenario match {
      case "HAPPY_PATH_1" => individualIncomeResponse shouldBe maximumModel
      case "HAPPY_PATH_2" => individualIncomeResponse shouldBe minimumModel
      case s              =>
        fail(s"[IndividualIncomeStepDefs][scenarioCheck] scenario $s does not match or exist")
    }
  }

  Then(
    """^I should get the statusCode: (.*) responseCode: (.*) responseMessage: (.*) when calling individual-income with UTR: (.*) and tax year: (.*)$"""
  ) { (statusCode: Int, responseCode: String, responseMessage: String, utr: String, taxYear: String) =>
    val NOT_ACCEPTABLE: Int = 406

    val response: Response =
      statusCode match {
        case NOT_ACCEPTABLE =>
          getIndividualIncomeResponse(utr, taxYear)("Authorization" -> LocalBearerGenerator.authBearerToken)
        case _              =>
          getIndividualIncomeResponse(utr, taxYear)(headers: _*)
      }

    val jsonResponseData: JsValue = response.data

    val code: String    = (jsonResponseData \ "code").as[JsString].value
    val message: String = (jsonResponseData \ "message").as[JsString].value

    code       shouldBe responseCode
    message    shouldBe responseMessage
    statusCode shouldBe response.status
  }
}
