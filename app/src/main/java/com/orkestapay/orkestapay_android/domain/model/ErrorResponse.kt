package com.orkestapay.orkestapay_android.domain.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    val step: String = "",
    @SerializedName("request_id") val requestId: String = "",
    val category: String = "",
    val message: String,
    val timestamp: String = "",
    val code: String = ""
)
