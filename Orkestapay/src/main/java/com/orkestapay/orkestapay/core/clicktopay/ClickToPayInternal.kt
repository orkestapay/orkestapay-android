package com.orkestapay.orkestapay.core.clicktopay

import android.net.Uri
import com.orkestapay.orkestapay.client.enums.CardType
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.CardResponse
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse

internal object ClickToPayInternal {
    var currentCallback: ClickToPayCallback? = null

    fun notifyResult(uri: Uri) {
        val status = uri.getQueryParameter("status")
        val message = uri.getQueryParameter("message") ?: ""

        when (status) {
            "success" -> currentCallback?.onSuccess(paymentMethod = PaymentMethodResponse(
                paymentMethodId = "",
                type = PaymentMethodType.CLICK_TO_PAY,
                card = CardResponse(
                    bin = "",
                    lastFour = "",
                    brand = "",
                    cardType = CardType.UNKNOWN,
                    expirationMonth = "",
                    expirationYear = "",
                    holderName = "",
                    holderLastName = "",
                    oneTimeUse = true
                ),
            ))
            "error" -> currentCallback?.onError(message)
            else -> currentCallback?.onError("Unknown status received")
        }

        currentCallback = null
    }
}