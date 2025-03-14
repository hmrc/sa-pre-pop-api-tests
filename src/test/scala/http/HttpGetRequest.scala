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

package http

import play.api.libs.ws.StandaloneWSResponse

trait HttpGetRequest extends HttpRequest {

  override def executeRestCall(url: String): StandaloneWSResponse = {
    logger.info(s"Executing GET request, on url=$url")

    val response = await(
      HttpClient
        .createRequest(url)
        .withHttpHeaders(headers: _*)
        .get()
    )

    printResponse(response)

    response
  }

  override def executeRestWithBodyCall(url: String, body: String): StandaloneWSResponse =
    throw new UnsupportedOperationException("Not supported")

}
