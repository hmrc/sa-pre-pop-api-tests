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

import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.ws.StandaloneWSResponse

import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, Future}

trait HttpRequest {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def headers: Seq[(String, String)]

  def executeRestCall(url: String): StandaloneWSResponse
  def executeRestWithBodyCall(url: String, body: String): StandaloneWSResponse

  def await(standaloneWSRequestF: Future[StandaloneWSResponse]): StandaloneWSResponse =
    Await.result(standaloneWSRequestF, Duration(30, SECONDS))

  def printResponse(response: StandaloneWSResponse): Unit = {
    logger.info(s"Request url=${response.uri.toURL.toString}")
    logger.info(s"Headers=${response.headers}")
    logger.info(s"Body=${response.body}")
  }

}
