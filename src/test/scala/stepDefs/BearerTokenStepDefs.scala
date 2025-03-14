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
import org.scalatest.matchers.should._
import pages.LocalBearerGenerator.obtainBearerToken

class BearerTokenStepDefs extends ScalaDsl with EN with Matchers {

  Given("""^I have generated a bearer token (.*) for the UTR '(.*)'$""") { (bearerToken: String, utr: String) =>
    obtainBearerToken(bearerToken, utr)
  }

}
