package com.orkestapay.orkestapay.client.model

import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodData(val type:PaymentMethodType, val name: String? = null, val properties: PaymentMethodProperties)
@Serializable
data class PaymentMethodProperties(@SerialName("merchant_id") val merchantId: String, val gateway: String, @SerialName("merchant_name") val merchantName: String)
