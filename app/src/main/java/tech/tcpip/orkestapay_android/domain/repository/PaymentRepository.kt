package tech.tcpip.orkestapay_android.domain.repository

import tech.tcpip.orkestapay_android.domain.model.PaymentRequest
import tech.tcpip.orkestapay_android.domain.model.Resource
import tech.tcpip.orkestapay_android.domain.model.SuccessResponse

interface PaymentRepository {
    suspend fun payment(paymentRequest: PaymentRequest): Resource<SuccessResponse>
}