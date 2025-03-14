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

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads}

case class IndividualTaxResponse(
  pensionsAnnuitiesAndOtherStateBenefits: StateBenefits,
  refunds: Refund,
  employments: List[IndividualTaxEmployment]
)

case class StateBenefits(otherPensionsAndRetirementAnnuities: Option[Double], incapacityBenefit: Option[Double])

case class Refund(taxRefundedOrSetOff: Option[Double])

case class IndividualTaxEmployment(employerPayeReference: String, taxTakenOffPay: Double)

object IndividualTaxResponse {

  implicit val individualTaxEmploymentReads: Reads[IndividualTaxEmployment] = (
    (JsPath \ "employerPayeReference").read[String] and
      (JsPath \ "taxTakenOffPay").read[Double]
  )(IndividualTaxEmployment.apply _)

  implicit val stateBenefitsReads: Reads[StateBenefits] = (
    (JsPath \ "otherPensionsAndRetirementAnnuities").readNullable[Double] and
      (JsPath \ "incapacityBenefit").readNullable[Double]
  )(StateBenefits.apply _)

  implicit val refundReads: Reads[Refund] = Json.format[Refund]

  implicit val individualTaxResponseReads: Reads[IndividualTaxResponse] = (
    (JsPath \ "pensionsAnnuitiesAndOtherStateBenefits").read[StateBenefits] and
      (JsPath \ "refunds").read[Refund] and
      (JsPath \ "employments").read[List[IndividualTaxEmployment]]
  )(IndividualTaxResponse.apply _)
}
