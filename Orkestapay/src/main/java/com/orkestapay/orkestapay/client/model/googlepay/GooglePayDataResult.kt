package com.orkestapay.orkestapay.client.model.googlepay

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class GooglePayDataResult(override val apiVersionMinor: Int,
                               override val apiVersion: Int,
                               override val paymentMethodData: PaymentMethodDataGooglePay
): GooglePayPaymentData()
