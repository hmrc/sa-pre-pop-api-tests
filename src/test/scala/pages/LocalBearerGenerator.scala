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

package pages

import config.Configuration
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.should._
import play.api.libs.ws.DefaultBodyWritables.writeableOf_String
import http.HttpClient._

object LocalBearerGenerator extends Matchers with ScalaFutures with IntegrationPatience {

  var authBearerToken = "_"

  def obtainBearerToken(bearerTokenType: String, utr: String): Unit =
    authBearerToken = bearerTokenType match {
      case "Valid"   => getBearerLocal(utr)
      case "Missing" => ""
      case _         => "Bearer DUMMY"
    }

  def putBodyLocal(utr: String): String = {
    lazy val enrolments: String =
      s""" [
         |   {
         |     "key": "IR-SA",
         |     "identifiers": [
         |       {
         |         "key": "UTR",
         |         "value": "$utr"
         |       }
         |     ],
         |     "state": "Activated"
         |   }
         |  ] """.stripMargin

    val authPayload =
      s"""
         | {
         |  "credentials": {
         |    "providerId": "81448533810644543",
         |    "providerType": "GovernmentGateway"
         |  },
         |  "confidenceLevel": 200,
         |  "nino": "CK562300D",
         |  "usersName": "test",
         |  "credentialRole": "User",
         |  "affinityGroup": "Individual",
         |  "credentialStrength": "strong",
         |  "credId": "453234543adr54hy9",
         |  "enrolments": $enrolments
         | }
     """.stripMargin

    authPayload
  }

  def getBearerLocal(utr: String): String = {
    val body     = putBodyLocal(utr)
    val response = createRequest(s"${Configuration.settings.AUTH_ROOT}/government-gateway/session/login")
      .withHttpHeaders("Content-Type" -> "application/json")
      .post(body)
      .futureValue
    val bearer   = response.headers("Authorization")

    printResponse(response)

    bearer.head
  }
}
