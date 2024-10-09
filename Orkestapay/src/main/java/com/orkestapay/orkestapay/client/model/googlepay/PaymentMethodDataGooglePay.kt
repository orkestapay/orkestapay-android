package com.orkestapay.orkestapay.client.model.googlepay

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class PaymentMethodDataGooglePay(val description: String,
                                      val type: String,
                                      @SerialName("tokenization_data") @JsonNames("tokenizationData") val tokenizationData: GooglePayPaymentMethodDataTokenizationData,
                                      val info: GooglePayPaymentMethodDataInfo)
