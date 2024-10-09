package com.orkestapay.orkestapay.core.googlepay

import com.orkestapay.orkestapay.client.model.PaymentMethodResponse

interface GooglePayCallback {
    fun onReady(isReady: Boolean)
    fun onSuccess(paymentMethod: PaymentMethodResponse)
    fun onCancel()
    fun onError(error: String)
}