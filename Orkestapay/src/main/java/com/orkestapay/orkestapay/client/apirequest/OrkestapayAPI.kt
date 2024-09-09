package com.orkestapay.orkestapay.client.apirequest

import android.util.Log
import com.orkestapay.orkestapay.client.model.PaymentMethod
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.PromotionsResponse
import com.orkestapay.orkestapay.core.networking.RestRequest
import com.orkestapay.orkestapay.core.networking.CoreConfig
import com.orkestapay.orkestapay.core.networking.model.ErrorResponse
import com.orkestapay.orkestapay.core.networking.HttpMethod
import com.orkestapay.orkestapay.core.networking.NetworkingClient
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

internal class OrkestapayAPI(private val coreConfig: CoreConfig) {
    private var networkingClient: NetworkingClient = NetworkingClient(coreConfig)

    suspend fun createPaymentMethod(paymentMethod: PaymentMethod, listener: PaymentMethodListener){
        if(paymentMethod.customerId.isNullOrEmpty() && paymentMethod.deviceSessionId.isNullOrEmpty()) {
            listener.onError(OrkestapayError(-1, "It is necessary to send customerId or deviceSessionId"))
        }
        val body = Json.encodeToString(paymentMethod)
        val restRequest = RestRequest("/v1/payment-methods", HttpMethod.POST, body)
        val httpResponse = networkingClient.send(restRequest)
        try {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            if(httpResponse.isSuccessful) {
                val response = json.decodeFromString<PaymentMethodResponse>(httpResponse.body.toString())
                listener.onSuccess(response)
            } else {
                val error = json.decodeFromString<ErrorResponse>(httpResponse.body.toString())
                var errorDescription = "${error.message ?: error.error}"
                if(!error.validationErrors.isNullOrEmpty()){
                    val errors = error.validationErrors.joinToString(", ") {
                         it.message
                    }
                    errorDescription += " [$errors]"
                }

                listener.onError(OrkestapayError(httpResponse.status, errorDescription))
            }

        } catch (e: Exception){
            listener.onError(OrkestapayError(httpResponse.status, e.message.toString()))
        }
    }

    suspend fun getPromotions(binNumber: String, currency: String, totalAmount: String, listener: PromotionsListener){
        val restRequest = RestRequest("/v1/merchants/${coreConfig.merchantId}/promotions?binNumber=$binNumber&currency=$currency&totalAmount=$totalAmount", HttpMethod.GET)
        val httpResponse = networkingClient.send(restRequest)
        try {
            val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
            if(httpResponse.isSuccessful) {
                val response = json.decodeFromString<List<PromotionsResponse>>(httpResponse.body.toString())
                listener.onSuccess(response)
            } else {
                val error = json.decodeFromString<ErrorResponse>(httpResponse.body.toString())
                listener.onError(OrkestapayError(httpResponse.status, error.message ?: error.error ?: ""))
            }

        } catch (e: Exception){
            listener.onError(OrkestapayError(httpResponse.status, e.message.toString()))
        }
    }
}