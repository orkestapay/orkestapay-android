package com.orkestapay.orkestapay.client.model

import com.orkestapay.orkestapay.client.enums.CardType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardResponse(val bin: String, @SerialName("last_four") val lastFour: String, val brand: String, @SerialName("card_type") val cardType: CardType, @SerialName("expiration_month") val expirationMonth: String, @SerialName("expiration_year") val expirationYear: String, @SerialName("holder_name") val holderName: String, @SerialName("holder_last_name") val holderLastName: String?, @SerialName("one_time_use") val oneTimeUse: Boolean)
