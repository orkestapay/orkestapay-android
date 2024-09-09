package com.orkestapay.orkestapay.core.clicktopay

import android.content.Context
import android.content.Intent
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.core.networking.CoreConfig
import com.orkestapay.orkestapay.core.networking.Environment

internal class ClickToPayClient(private val coreConfig: CoreConfig) {

    fun openClickToPayCheckout(context: Context, clickToPay: ClickToPay, callback: ClickToPayListener) {
        WebviewActivity.setListener(callback)
        val intent = Intent(context, WebviewActivity::class.java).apply {
            putExtra(WebviewActivity.CLICK_TO_PAY, clickToPay)
            putExtra(WebviewActivity.MERCHANT_ID, coreConfig.merchantId)
            putExtra(WebviewActivity.PUBLIC_KEY, coreConfig.publicKey)
            putExtra(WebviewActivity.URL, coreConfig.environment.checkoutUrl)
        }
        context.startActivity(intent)
    }
}