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

case class Configuration(
  APP_BENEFITS_ROOT: String,
  APP_EMPLOYMENT_ROOT: String,
  APP_INCOME_ROOT: String,
  APP_TAX_ROOT: String,
  STUB_ROOT: String,
  AUTH_ROOT: String
)

object Configuration {

  lazy val settings: Configuration = create()

  def create(): Configuration = {
    val environmentProperty = System.getProperty("environment", "local")

    environmentProperty match {
      case "local" =>
        new Configuration(
          APP_BENEFITS_ROOT = "http://localhost:9621",
          APP_EMPLOYMENT_ROOT = "http://localhost:9622",
          APP_INCOME_ROOT = "http://localhost:9623",
          APP_TAX_ROOT = "http://localhost:9624",
          STUB_ROOT = "http://localhost:9689",
          AUTH_ROOT = "http://localhost:8585"
        )
      case _       => throw new IllegalArgumentException(s"Environment '$environmentProperty' not known")
    }
  }

}
