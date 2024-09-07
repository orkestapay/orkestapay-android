package com.orkestapay.orkestapay.client.model

import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPayMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethod(val alias: String?,
                         @SerialName("customer_id") val customerId: String?,
                         @SerialName("device_session_id") val deviceSessionId: String?,
                         val type: PaymentMethodType,
                         val card: Card? = null,
                         val clickToPay: ClickToPayMethod? = null,
                         @SerialName("billing_address") val billingAddress: BillingAddress? = null)
