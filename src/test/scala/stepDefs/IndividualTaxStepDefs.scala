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
import models.{IndividualTaxEmployment, IndividualTaxResponse, Refund, StateBenefits}
import org.scalatest.matchers.should.Matchers
import requests.CreateTestUser.createTestUserBenefits
import requests.GetIndividualTax
import requests.GetIndividualTax.*
import play.api.libs.json.{JsString, JsValue}

class IndividualTaxStepDefs extends ScalaDsl with EN with Matchers {

  val maximumModel: IndividualTaxResponse = IndividualTaxResponse(
    StateBenefits(None, None),
    Refund(None),
    List(IndividualTaxEmployment(employerPayeReference = "111/AAA", taxTakenOffPay = 3000))
  )

  val minimumModel: IndividualTaxResponse = IndividualTaxResponse(
    StateBenefits(otherPensionsAndRetirementAnnuities = Option(36.50), incapacityBenefit = Option(980.45)),
    Refund(taxRefundedOrSetOff = Option(325.00)),
    List(
      IndividualTaxEmployment(employerPayeReference = "123/AB456", taxTakenOffPay = 890.35),
      IndividualTaxEmployment(employerPayeReference = "456/AB456", taxTakenOffPay = 224.99)
    )
  )

  Given(
    """^I have generated a test user for individual-tax with the UTR: (.*) for: (.*) with scenario: (.*)$"""
  ) { (utr: String, taxYear: String, scenario: String) =>
    createTestUserBenefits(utr, taxYear, scenario, "tax")
  }

  Then(
    """^I should get the '(.*)' success status when calling individual-tax with UTR:'(.*)' and tax year: '(.*)' for scenario: '(.*)'$"""
  ) { (statusCode: Int, utr: String, taxYear: String, scenario: String) =>
    val response = new GetIndividualTax().getIndividualTaxResponse(utr, taxYear)
    statusCode shouldBe response.status

    val parsedBody = response.data.as[IndividualTaxResponse]

    scenario match {
      case "HAPPY_PATH_1" => parsedBody shouldBe minimumModel
      case "HAPPY_PATH_2" => parsedBody shouldBe maximumModel
      case f              => fail(s"no valid json template with the name: $f")
    }
  }

  Then("""^I should get the '(.*)' '(.*)' '(.*)' when calling individual-tax with UTR:'(.*)' and tax year: '(.*)'$""") {
    (statusCode: Int, responseCode: String, responseMessage: String, utr: String, taxYear: String) =>
      val response =
        statusCode match {
          case 406 => new GetIndividualTax(headers = Seq.empty).getIndividualTaxResponse(utr, taxYear)
          case _   => new GetIndividualTax().getIndividualTaxResponse(utr, taxYear)
        }

      val jsonData: JsValue = response.data

      val code    = (jsonData \ "code").as[JsString].value
      val message = (jsonData \ "message").as[JsString].value

      responseCode    shouldBe code
      message         shouldBe responseMessage
      response.status shouldBe statusCode
  }
}
