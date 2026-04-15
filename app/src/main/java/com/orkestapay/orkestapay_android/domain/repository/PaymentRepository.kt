package com.orkestapay.orkestapay_android.domain.repository

import com.orkestapay.orkestapay_android.domain.model.PaymentRequest
import com.orkestapay.orkestapay_android.domain.model.Resource
import com.orkestapay.orkestapay_android.domain.model.SuccessResponse

interface PaymentRepository {
    suspend fun payment(paymentRequest: PaymentRequest): Resource<SuccessResponse>
}