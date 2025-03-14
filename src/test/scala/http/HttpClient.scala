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

import config.Zap
import org.apache.pekko.actor.ActorSystem
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{StandaloneWSRequest, StandaloneWSResponse}

object HttpClient {

  private val logger: Logger = LoggerFactory.getLogger(HttpClient.getClass)

  implicit val actorSystem: ActorSystem = ActorSystem()
  val httpClient: StandaloneAhcWSClient = StandaloneAhcWSClient()

  def createRequest(url: String): StandaloneWSRequest = {
    val req = httpClient.url(url)

    Zap.getProxyIfEnabled.map(req.withProxyServer(_)).getOrElse(req)
  }

  def printResponse(response: StandaloneWSResponse): Unit = {
    logger.info(s"Request url=${response.uri.toURL.toString}")
    logger.info(s"Headers=${response.headers}")
    logger.info(s"Body=${response.body}")
  }

}
