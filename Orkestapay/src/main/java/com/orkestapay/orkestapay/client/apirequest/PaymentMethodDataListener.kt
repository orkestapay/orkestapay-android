package com.orkestapay.orkestapay.client.apirequest

import com.orkestapay.orkestapay.client.model.PaymentMethodData
import com.orkestapay.orkestapay.core.networking.OrkestapayError

interface PaymentMethodDataListener {
    fun onSuccess(paymentMethodData: PaymentMethodData)

    fun onError(error: OrkestapayError)
}