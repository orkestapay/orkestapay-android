package com.orkestapay.orkestapay.client.model.googlepay

import java.io.Serializable

data class GooglePayData(val amount: String,
                         val currencyCode: String,
                         val countryCode: String,
                         val isSandbox: Boolean? = true): Serializable
