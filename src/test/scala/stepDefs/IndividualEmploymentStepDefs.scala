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
import models.{IndividualEmployment, IndividualEmploymentResponse}
import org.scalatest.matchers.should.Matchers
import requests.CreateTestUser.createTestUserBenefits
import requests.GetIndividualEmployment.*
import requests.{GetIndividualEmployment, LocalBearerGenerator}
import play.api.libs.json.JsString

class IndividualEmploymentStepDefs extends ScalaDsl with EN with Matchers {

  val maximumModel: IndividualEmploymentResponse =
    IndividualEmploymentResponse(
      List(
        IndividualEmployment(
          employerPayeReference = "123/AB456",
          employerName = "Company XYZ",
          offPayrollWorkFlag = Some(true)
        ),
        IndividualEmployment(
          employerPayeReference = "456/AB456",
          employerName = "Company ABC",
          offPayrollWorkFlag = Some(false)
        )
      )
    )

  val minimumModel: IndividualEmploymentResponse =
    IndividualEmploymentResponse(
      List(
        IndividualEmployment(
          employerPayeReference = "123/AB456",
          employerName = "Company XYZ",
          offPayrollWorkFlag = None
        )
      )
    )

  Given(
    """^I have generated a test user for individual-employments with the UTR: (.*) for: (.*) with scenario: (.*)$"""
  ) { (utr: String, taxYear: String, scenario: String) =>
    createTestUserBenefits(utr, taxYear, scenario, "employments")
  }

  Then(
    """^I should get the (.*) success status when calling individual-employment with UTR: (.*) and tax year: (.*) for scenario: (.*)$"""
  ) { (statusCode: Int, utr: String, taxYear: String, scenario: String) =>
    val response         = new GetIndividualEmployment().getIndividualEmploymentResponse(utr, taxYear)
    val jsonResponseData = response.data

    val individualEmploymentResponse: IndividualEmploymentResponse = jsonResponseData.as[IndividualEmploymentResponse]

    assert(statusCode == response.status, s"Expected status code $statusCode but got ${response.status}")

    scenario match {
      case "HAPPY_PATH_1" => individualEmploymentResponse shouldBe maximumModel
      case "HAPPY_PATH_2" => individualEmploymentResponse shouldBe minimumModel
      case s              =>
        fail(s"[IndividualEmploymentStepDefs][scenarioCheck] scenario $s does not match or exist")
    }
  }

  Then(
    """^I should get the statusCode: (.*) responseCode: (.*) responseMessage: (.*) when calling individual-employment with UTR: (.*) and tax year: (.*)$"""
  ) { (statusCode: Int, responseCode: String, responseMessage: String, utr: String, taxYear: String) =>
    val NOT_ACCEPTABLE = 406
    val response       =
      statusCode match {
        case NOT_ACCEPTABLE =>
          new GetIndividualEmployment(headers = Seq("Authorization" -> LocalBearerGenerator.authBearerToken))
            .getIndividualEmploymentResponse(utr, taxYear)
        case _              =>
          new GetIndividualEmployment().getIndividualEmploymentResponse(utr, taxYear)
      }

    val jsonResponseData = response.data

    val code    = (jsonResponseData \ "code").as[JsString].value
    val message = (jsonResponseData \ "message").as[JsString].value

    assert(code == responseCode, s"Expected code: $responseCode, but got: $code")
    assert(message == responseMessage, s"Expected message: $responseMessage, but got: $message")
    assert(statusCode == response.status, s"Expected status code: $statusCode, but got: ${response.status}")
  }
}
