package com.orkestapay.orkestapay.core.networking

data class CoreConfig @JvmOverloads constructor(
    val merchantId: String,
    val publicKey: String,
    val environment: Environment = Environment.SANDBOX,
)