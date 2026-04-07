package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class PaymentOption (@SerialName("promotion_id") val promotionId: String,
                      @SerialName("promotion_name") val promotionName: String,
                      val type: String? = null,
                      val installments: Int? = null,
                      val issuerId: String? = null,
                      val issuerName: String? = null)