package tech.tcpip.orkestapay_android.domain.model

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    val amount: Double,
    val email: String,
    @SerializedName("payment_method_id") val paymentMethodId: String,
    @SerializedName("device_session_id") val deviceSession: String
)
