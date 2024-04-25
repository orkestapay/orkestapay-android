package com.orkestapay.orkestapay.client.model

import kotlinx.serialization.Serializable

@Serializable
data class PromotionsResponse(val type: String, val installments: List<Int>)
