package com.orkestapay.orkestapay.client.apirequest

import com.orkestapay.orkestapay.client.model.PromotionsResponse
import com.orkestapay.orkestapay.core.networking.OrkestapayError

interface PromotionsListener {
    fun onSuccess(promotions: List<PromotionsResponse>)
    fun onError(error: OrkestapayError)
}