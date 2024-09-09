package com.orkestapay.orkestapay.core.clicktopay

interface ClickToPayListener {
    fun onSuccess(paymentId: String)

    fun onClosed()
    fun onError(error: String)
}
