package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillingAddress(@SerialName("first_name") val firstName: String?, @SerialName("last_name") val lastName: String? = null, val email: String?,
                          val phone :Phone?, val type: String? = null, @SerialName("line_1") val line1: String, @SerialName("line_2") val line2: String? = null,
                          val city: String, val state: String, val country: String, @SerialName("zip_code") val zipCode: String)
