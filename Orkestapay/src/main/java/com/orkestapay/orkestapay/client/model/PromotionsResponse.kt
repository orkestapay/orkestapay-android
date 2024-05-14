package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionsResponse(@SerialName("promotion_id") val promotionId: String,
                              @SerialName("promotion_name") val promotionName : String,
                              val type: String,
                              val installments: List<Int>,
                              @SerialName("issuer_id") val issuerId: String? = null,
                              @SerialName("issuer_name") val issuerName: String? = null,
                              @SerialName("currency_code") val currencyCode: String? = null,
                              @SerialName("minimum_amount") val minimumAmount: Int? = null,
                              @SerialName("maximum_amount")  val maximumAmount: Int? = null)
