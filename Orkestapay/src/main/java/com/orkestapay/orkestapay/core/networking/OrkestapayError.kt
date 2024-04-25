package com.orkestapay.orkestapay.core.networking

data class OrkestapayError(
    val code: Int,
    val errorDescription: String,
) : Exception("Error: $code - Description: $errorDescription")