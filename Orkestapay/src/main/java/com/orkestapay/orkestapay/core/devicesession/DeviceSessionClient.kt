package com.orkestapay.orkestapay.core.devicesession

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.orkestapay.orkestapay.core.networking.CoreConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Timer
import kotlin.concurrent.timerTask

internal class DeviceSessionClient(private val coreConfig: CoreConfig) {

    @SuppressLint("SetJavaScriptEnabled")
    fun getDeviceSessionId(context: Context, callback: DeviceSessionListener){
        val urlString = coreConfig.environment.checkoutUrl
        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JsInterface(callback), "androidListener")
            clearCache(true)
            clearHistory()
            loadUrl("$urlString/script/device-session?merchant_id=${coreConfig.merchantId}&public_key=${coreConfig.publicKey}")
        }
        Timer().schedule(timerTask {
            CoroutineScope(Dispatchers.Main).launch {
                webView.reload()
            }
        },10000)
    }
}

internal class JsInterface(private val callback: DeviceSessionListener) {
    @JavascriptInterface
    fun receiveMessage(value: String) {
        val jsonObject = JSONObject(value)
        if(jsonObject.has("device_session_id")) {
            callback.onSuccess(jsonObject.get("device_session_id").toString())
        }
    }
}