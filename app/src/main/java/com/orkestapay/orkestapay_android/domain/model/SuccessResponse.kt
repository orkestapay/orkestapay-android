package com.orkestapay.orkestapay_android.domain.model

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("payment_id") val paymentId: String,
    @SerializedName("order_id") val orderId: String,
    @SerializedName("merchant_order_id") val merchantOrderId: String,
    val currency: String,
    @SerializedName("total_amount") val totalAmount: String,
    val status: String,
    val customer: Customer,
    val card: Card,
    @SerializedName("created_at") val createdAt: String,
)