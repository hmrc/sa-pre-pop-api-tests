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
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsValue, Json}
import http.HttpClient.*
import models.Response

object GetIndividualTax extends Matchers with ScalaFutures with IntegrationPatience {

  def headers: Seq[(String, String)] =
    Seq(
      ("Accept", "application/vnd.hmrc.1.1+json"),
      ("Authorization", LocalBearerGenerator.authBearerToken)
    )

  def getIndividualTaxResponse(utr: String, taxYear: String)(headers: (String, String)*): Response = {
    val getURL               = s"${Configuration.settings.APP_TAX_ROOT}/sa/$utr/annual-summary/$taxYear"
    val response             = createRequest(getURL)
      .withHttpHeaders(headers: _*)
      .get()
      .futureValue
    val jsonResponse: String = response.body
    val data: JsValue        = Json.parse(jsonResponse)

    printResponse(response)

    Response(response.status, data)
  }
}
