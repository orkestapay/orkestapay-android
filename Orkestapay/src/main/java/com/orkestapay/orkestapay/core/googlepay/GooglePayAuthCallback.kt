package com.orkestapay.orkestapay.core.googlepay

import com.orkestapay.orkestapay.client.model.googlepay.PaymentMethodGooglePay

interface GooglePayAuthCallback {
    fun onSuccess(paymentMethod: PaymentMethodGooglePay, activity: GooglePayActivity)
}