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

package requests

import config.Configuration
import http.HttpPostRequest

object CreateTestUser extends HttpPostRequest {

  override def headers: Seq[(String, String)] = Seq(
    ("Accept", "application/vnd.hmrc.1.0+json"),
    ("Content-Type", "application/json")
  )

  def createTestUserBenefits(utr: String, taxYear: String, scenario: String, urlPath: String): Int = {
    val url  = s"${Configuration.settings.STUB_ROOT}/sa/$utr/$urlPath/annual-summary/$taxYear"
    val body = s"""{
                  |"scenario": "$scenario"
                  |}""".stripMargin

    executeRestWithBodyCall(url, body).status
  }

}
