package com.orkestapay.orkestapay_android.domain.model

import com.google.gson.annotations.SerializedName

data class Card(
    val bin: String,
    @SerializedName("last_four") val lastFour: String,
    val brand: String,
    @SerializedName("card_type") val cardType: String,
    @SerializedName("holder_name") val holderName: String,
    @SerializedName("holder_last_name") val holderLastName: String,
    @SerializedName("expiration_month") val expirationMonth: String,
    @SerializedName("expiration_year") val expirationYear: String,
    @SerializedName("issuer_bank_name") val issuerBankName: String,
    @SerializedName("one_time_use") val oneTimeUse: Boolean,
)
