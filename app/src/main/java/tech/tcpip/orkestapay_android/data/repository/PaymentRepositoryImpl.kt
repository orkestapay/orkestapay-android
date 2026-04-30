package tech.tcpip.orkestapay_android.data.repository

import com.google.gson.Gson
import tech.tcpip.orkestapay_android.data.remote.PaymentsApi
import tech.tcpip.orkestapay_android.domain.model.ErrorResponse
import tech.tcpip.orkestapay_android.domain.model.PaymentRequest
import tech.tcpip.orkestapay_android.domain.model.Resource
import tech.tcpip.orkestapay_android.domain.model.SuccessResponse
import tech.tcpip.orkestapay_android.domain.repository.PaymentRepository

class PaymentRepositoryImpl (private val api: PaymentsApi):
    PaymentRepository {
    override suspend fun payment(paymentRequest: PaymentRequest): Resource<SuccessResponse> {
        return try {
            val response = api.payment(
                key = "ork_ae01261b644622c876e54670e621fb2d",
                paymentRequest = paymentRequest
            )

            if (response.isSuccessful) {
                return Resource.Success(response.body()!!)
            } else {
                val errorJson = response.errorBody()?.string()

                val errorObj = Gson().fromJson(errorJson, ErrorResponse::class.java)
                return Resource.Error(errorObj)

            }
        } catch (e: Exception) {
            Resource.Error(ErrorResponse(code = "UNKNOWN_ERROR", message = e.message ?: "Error desconocido"))
        }

    }
}