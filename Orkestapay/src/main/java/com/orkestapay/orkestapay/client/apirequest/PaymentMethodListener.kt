package com.orkestapay.orkestapay.client.apirequest

import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.core.networking.OrkestapayError

interface PaymentMethodListener {
    fun onSuccess(paymentMethod: PaymentMethodResponse)
    fun onError(error: OrkestapayError)
}