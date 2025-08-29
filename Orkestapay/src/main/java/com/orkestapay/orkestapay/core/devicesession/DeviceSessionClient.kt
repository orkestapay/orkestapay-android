package com.orkestapay.orkestapay.core.devicesession

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.orkestapay.orkestapay.core.networking.CoreConfig
import org.json.JSONObject


private var hasSession = false
internal class DeviceSessionClient(private val coreConfig: CoreConfig) {

    @SuppressLint("SetJavaScriptEnabled")
    fun getDeviceSessionId(context: Context, parent: ViewGroup, callback: DeviceSessionListener){
        val urlString = coreConfig.environment.checkoutUrl

        val webView = WebView(context).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JsInterface(callback, this), "androidListener")
            clearCache(true)
            // clearHistory()
            webViewClient = WebViewClient()

            /*webViewClient = object : WebViewClient() {
                override fun onRenderProcessGone(view: WebView, detail: RenderProcessGoneDetail): Boolean {
                    Log.e("WebView", "Render process crashed: ${detail}")
                    return true
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    Log.d("WebView", "JS Log: ${consoleMessage?.message()}")
                    return super.onConsoleMessage(consoleMessage)
                }
            }*/

            loadUrl("$urlString/script/device-session?merchant_id=${coreConfig.merchantId}&public_key=${coreConfig.publicKey}")
        }

        parent.addView(webView)
    }
}

internal class JsInterface(private val callback: DeviceSessionListener, private val webView: WebView) {
    @JavascriptInterface
    fun receiveMessage(value: String) {
        val jsonObject = JSONObject(value)
        if(jsonObject.has("device_session_id")) {
            hasSession = true
            callback.onSuccess(jsonObject.get("device_session_id").toString())
            webView.destroy()
        }
        if(jsonObject.has("error")) {
            hasSession = true
            callback.onError(jsonObject.getJSONObject("error").get("message").toString())
            webView.destroy()
        }
    }
}