package com.orkestapay.orkestapay.core.clicktopay

import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
interface ClickToPayCallback {
    fun onSuccess(paymentMethod: PaymentMethodResponse)
    fun onClosed()
    fun onError(error: String)
}
