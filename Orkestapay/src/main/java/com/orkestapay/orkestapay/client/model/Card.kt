package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Card(val number: String,
                @SerialName("expiration_month") val expirationMonth: String,
                @SerialName("expiration_year") val expirationYear: String,
                val cvv: String,
                @SerialName("holder_name") val holderName: String,
                @SerialName("one_time_use") val oneTimeUse: Boolean)