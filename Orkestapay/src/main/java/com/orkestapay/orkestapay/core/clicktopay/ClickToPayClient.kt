package com.orkestapay.orkestapay.core.clicktopay


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.core.networking.CoreConfig
import java.net.URLEncoder

internal class ClickToPayClient(private val coreConfig: CoreConfig) {

    fun openClickToPayCheckout(context: Context,clickToPay: ClickToPay/*, style: ClickToPayStyle?*/, callback: ClickToPayCallback) {
        /* WebviewActivity.setListener(callback)
        val intent = Intent(context, WebviewActivity::class.java).apply {
            putExtra(WebviewActivity.CLICK_TO_PAY, clickToPay)
            putExtra(WebviewActivity.MERCHANT_ID, coreConfig.merchantId)
            putExtra(WebviewActivity.PUBLIC_KEY, coreConfig.publicKey)
            putExtra(WebviewActivity.URL, coreConfig.environment.checkoutUrl)
        }
        if (style != null) {
            intent.putExtra(WebviewActivity.STYLE, style)
        }
        context.startActivity(intent) */

        ClickToPayInternal.currentCallback = callback

        var urlCheckout = "${coreConfig.environment.checkoutUrl}/integrations/click-to-pay"
        urlCheckout = addQueryParam(urlCheckout, "merchantId", coreConfig.merchantId, isFirst = true)
        urlCheckout = addQueryParam(urlCheckout, "publicKey", coreConfig.publicKey)
        urlCheckout = addQueryParam(urlCheckout, "currency", clickToPay.currency)
        urlCheckout = addQueryParam(urlCheckout, "totalAmount", clickToPay.totalAmount)
        urlCheckout = addQueryParam(urlCheckout, "email", clickToPay.email)
        urlCheckout = addQueryParam(urlCheckout, "phoneCountryCode", clickToPay.phoneCountryCode)
        urlCheckout = addQueryParam(urlCheckout, "phoneNumber", clickToPay.phoneNumber)
        urlCheckout = addQueryParam(urlCheckout, "firstName", clickToPay.firstName)
        urlCheckout = addQueryParam(urlCheckout, "lastName", clickToPay.lastName)


        val intent = Intent(context, ClickToPayControlActivity::class.java).apply {
            putExtra("EXTRA_URL", urlCheckout)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }


    private fun addQueryParam(url: String, name: String, value: String?, isFirst: Boolean = false): String {
        var newUrl = url

        if(!value.isNullOrEmpty()) {
            newUrl += if (isFirst) "?" else "&"
            newUrl +=  "${name}=${URLEncoder.encode(value, "UTF-8")}"
        }
        return newUrl
    }

}