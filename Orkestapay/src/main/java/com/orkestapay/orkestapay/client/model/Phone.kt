package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Phone(val number: String, @SerialName("country_code") val countryCode: String)
