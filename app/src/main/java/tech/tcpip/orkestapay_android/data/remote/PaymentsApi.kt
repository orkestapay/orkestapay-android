package tech.tcpip.orkestapay_android.data.remote

import tech.tcpip.orkestapay_android.domain.model.PaymentRequest
import tech.tcpip.orkestapay_android.domain.model.SuccessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentsApi {
    @POST("api/certification/payments")
    suspend fun payment(
        @Header("x-api-key") key: String,
        @Body paymentRequest: PaymentRequest
    ): Response<SuccessResponse>
}