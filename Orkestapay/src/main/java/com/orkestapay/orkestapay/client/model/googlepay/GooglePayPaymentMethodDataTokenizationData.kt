package com.orkestapay.orkestapay.client.model.googlepay

import com.orkestapay.orkestapay.client.enums.googlepay.TokenizationSpecificationType
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class GooglePayPaymentMethodDataTokenizationData(val type: TokenizationSpecificationType, val token: String)
