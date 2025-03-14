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

package models

import play.api.libs.json.{Json, OFormat}

case class Employment(employerPayeReference: String, payFromEmployment: Double)

case class PensionsAnnuitiesAndOtherStateBenefits(
  otherPensionsAndRetirementAnnuities: Option[Double],
  incapacityBenefit: Option[Double],
  jobseekersAllowance: Option[Double],
  seissNetPaid: Option[Double]
)

case class IndividualIncomeResponse(
  pensionsAnnuitiesAndOtherStateBenefits: PensionsAnnuitiesAndOtherStateBenefits,
  employments: List[Employment]
)

object Employment {
  implicit val format: OFormat[Employment] = Json.format[Employment]
}

object PensionsAnnuitiesAndOtherStateBenefits {
  implicit val format: OFormat[PensionsAnnuitiesAndOtherStateBenefits] =
    Json.format[PensionsAnnuitiesAndOtherStateBenefits]
}

object IndividualIncomeResponse {
  implicit val format: OFormat[IndividualIncomeResponse] = Json.format[IndividualIncomeResponse]
}
