package com.orkestapay.orkestapay.client.model.googlepay

import kotlinx.serialization.Serializable

@Serializable
sealed class GooglePayPaymentData {
    abstract val apiVersionMinor: Int
    abstract val apiVersion: Int
    abstract val paymentMethodData: PaymentMethodDataGooglePay
}
