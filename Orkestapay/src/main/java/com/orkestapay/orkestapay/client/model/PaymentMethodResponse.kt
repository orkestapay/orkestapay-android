package com.orkestapay.orkestapay.client.model

import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodResponse(@SerialName("payment_method_id") val paymentMethodId: String,
                                 val alias: String,
                                 val type: PaymentMethodType,
                                 val card: CardResponse)
