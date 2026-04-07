package com.orkestapay.orkestapay.core.clicktopay

import android.net.Uri
import android.util.Log
import com.orkestapay.orkestapay.client.enums.CardType
import com.orkestapay.orkestapay.client.enums.ClickToPayEvent
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.CardResponse
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse

internal object ClickToPayInternal {
    var currentCallback: ClickToPayCallback? = null

    fun notifyResult(uri: Uri) {
        val status = uri.getQueryParameter("status") ?: "ERROR"
        val event = ClickToPayEvent.valueOf(status)
        when (event) {
            ClickToPayEvent.COMPLETE -> {
                val paymentMethodId = uri.getQueryParameter("payment_method_id") ?: ""
                val type = PaymentMethodType.valueOf(uri.getQueryParameter("type") ?: "CLICK_TO_PAY")
                val bin = uri.getQueryParameter("bin") ?: ""
                val lastFour = uri.getQueryParameter("last_four") ?: ""
                val brand = uri.getQueryParameter("brand") ?: ""
                val cardType = CardType.valueOf(uri.getQueryParameter("card_type") ?: "UNKNOWN")
                val holderName = uri.getQueryParameter("holder_name") ?: ""
                val expirationMonth = uri.getQueryParameter("expiration_month") ?: ""
                val expirationYear = uri.getQueryParameter("expiration_year") ?: ""
                val oneTimeUse = uri.getBooleanQueryParameter("one_time_use", true)
                currentCallback?.onSuccess(paymentMethod = PaymentMethodResponse(
                    paymentMethodId = paymentMethodId,
                    type = type,
                    card = CardResponse(
                        bin = bin,
                        lastFour = lastFour,
                        brand = brand,
                        cardType = cardType,
                        expirationMonth = expirationMonth,
                        expirationYear = expirationYear,
                        holderName = holderName,
                        holderLastName = null,
                        oneTimeUse = oneTimeUse
                    ),
                ))
            }

            ClickToPayEvent.ERROR -> {
                val message = uri.getQueryParameter("message") ?: ""
                currentCallback?.onError(message)
            }
            ClickToPayEvent.CANCEL ->  currentCallback?.onClosed()
        }

        currentCallback = null
    }
}