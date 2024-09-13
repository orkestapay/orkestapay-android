package com.orkestapay.orkestapay.client.model.googlepay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class GooglePayPaymentMethodDataInfo(@SerialName("card_network") @JsonNames("cardNetwork") val cardNetwork: String, @SerialName("card_details") @JsonNames("cardDetails") val cardDetails: String)
