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

package config

import play.api.libs.ws.DefaultWSProxyServer

object Zap {
  val proxyPort: Int     = System.getProperty("zap.proxyPort", "11000").toInt
  val isEnabled: Boolean = System.getProperty("security.assessment", "false") == "true"

  def getProxyIfEnabled: Option[DefaultWSProxyServer] =
    if (isEnabled) {
      Some(DefaultWSProxyServer("localhost", proxyPort))
    } else {
      None
    }
}
