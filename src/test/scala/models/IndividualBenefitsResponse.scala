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

case class IndividualBenefitsEmployment(
  employerPayeReference: String,
  companyCarsAndVansBenefit: Option[Double] = None,
  fuelForCompanyCarsAndVansBenefit: Option[Double] = None,
  privateMedicalDentalInsurance: Option[Double] = None,
  vouchersCreditCardsExcessMileageAllowance: Option[Double] = None,
  goodsEtcProvidedByEmployer: Option[Double] = None,
  accommodationProvidedByEmployer: Option[Double] = None,
  otherBenefits: Option[Double] = None,
  expensesPaymentsReceived: Option[Double] = None
)

case class IndividualBenefitsResponse(employments: List[IndividualBenefitsEmployment])

object IndividualBenefitsEmployment {
  implicit val format: OFormat[IndividualBenefitsEmployment] = Json.format[IndividualBenefitsEmployment]
}

object IndividualBenefitsResponse {
  implicit val format: OFormat[IndividualBenefitsResponse] = Json.format[IndividualBenefitsResponse]
}
