package com.orkestapay.orkestapay.core.networking

enum class Environment(internal val url: String, internal val checkoutUrl: String) {
    PRODUCTION(
        "https://api.orkestapay.com",
        "https://checkout.orkestapay.com"
    ),
    SANDBOX(
        "https://api.uat.orkestapay.com",
        "https://checkout.uat.orkestapay.com"
    ),
}
