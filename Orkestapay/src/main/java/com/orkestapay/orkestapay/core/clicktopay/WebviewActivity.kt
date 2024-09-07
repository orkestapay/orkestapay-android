package com.orkestapay.orkestapay.core.clicktopay

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.orkestapay.R
import com.orkestapay.orkestapay.client.enums.ClickToPayError
import com.orkestapay.orkestapay.client.enums.ClickToPayEvent
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.core.networking.NetworkUtils
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.Serializable


class WebviewActivity : ComponentActivity() {
    lateinit var webView: WebView
    lateinit var loader: ProgressBar
    private lateinit var clickToPay: ClickToPay
    private lateinit var merchantId: String
    private lateinit var publicKey: String
    private lateinit var isSandbox: String

    val callback: ClickToPayListener?
        get() {
            return listener
        }

    companion object {
        const val CLICK_TO_PAY = "CLICK_TO_PAY"
        const val MERCHANT_ID = "MERCHANT_ID"
        const val PUBLIC_KEY = "PUBLIC_KEY"
        const val IS_SANDBOX = "IS_SANDBOX"

        private var listener: ClickToPayListener? = null
        fun setListener(callback: ClickToPayListener) {
            listener = callback
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clickToPay = getSerializable(this, CLICK_TO_PAY, ClickToPay::class.java)
        merchantId = intent.getStringExtra(MERCHANT_ID)!!
        publicKey = intent.getStringExtra(PUBLIC_KEY)!!
        isSandbox = intent.getStringExtra(IS_SANDBOX)!!

        if (!NetworkUtils(this).hasInternet){
            callback?.onError(ClickToPayError.NO_INTERNET_CONNECTION.message)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                callback?.onClosed()
                finish()
            }

        })

        webView = findViewById(R.id.click_to_pay_web_view)
        loader = findViewById(R.id.loader)
        loadCheckout("")

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadCheckout(javascriptToInject: String? = null) {
        var urlCheckout = "https://checkout.dev.orkestapay.com/integrations/click2pay/#/checkout/${merchantId}/${publicKey}/${clickToPay.srcDpaId}/${clickToPay.dpaLocale}?dpaName=${clickToPay.dpaName}&email=${clickToPay.email}&isSandbox=${isSandbox}"
        urlCheckout = addQueryParamFromList(urlCheckout, "cardBrands", clickToPay.cardBrands)
        urlCheckout = addQueryParam(urlCheckout, "phoneCountryCode", clickToPay.phoneCountryCode)
        urlCheckout = addQueryParam(urlCheckout, "phoneNumber", clickToPay.phoneNumber)
        urlCheckout = addQueryParam(urlCheckout, "firstName", clickToPay.firstName)
        urlCheckout = addQueryParam(urlCheckout, "lastName", clickToPay.lastName)

        Log.d("url checkout", urlCheckout)

        webView.settings.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
            setSupportMultipleWindows(true)
        }

        // Add JavascriptInterface
        callback?.let { webView.addJavascriptInterface(JsInterface(it, this), "androidListener") }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                loader.visibility = View.GONE
                view?.visibility = View.VISIBLE
                callback?.onSuccess("success")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                if (error != null) {
                    Log.d("error webview", error.toString())
                }
            }
        }
        //setupWebChromeClient(webView)
        webView.loadUrl(urlCheckout)
    }

    private fun addQueryParam(url: String, name: String, value: String?): String {
        var newUrl = url
        if(!value.isNullOrEmpty()) {
            newUrl += "&${name}=${value}"
        }
        return newUrl
    }

    private fun addQueryParamFromList(url: String, name: String, value: List<String>): String {
        var newUrl = url
        value.forEach {
            newUrl += "&${name}=${it}"
        }
        return newUrl
    }
    @Suppress("UNCHECKED_CAST")
    private fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            activity.intent.getSerializableExtra(name, clazz)!!
        else
            activity.intent.getSerializableExtra(name) as T
    }
}

internal class JsInterface(private val callback: ClickToPayListener, val activity: ComponentActivity) {
    @JavascriptInterface
    fun receiveMessage(value: String) {
        try {
            val jsonObject = JSONObject(value)
            Log.d("message", jsonObject.toString())

            val status = jsonObject["status"] as? String
            var data: Json? = null
            var error: Json? = null
            if (jsonObject.has("data")) {
                data = jsonObject["data"] as? Json
            }
            if (jsonObject.has("error")) {
                data = jsonObject["error"] as? Json
            }

            if (status == null) {
                return
            }

            val event = ClickToPayEvent.valueOf(status)

            when (event) {
                ClickToPayEvent.COMPLETE -> {
                    callback.onSuccess(data.toString())
                }

                ClickToPayEvent.ERROR -> {
                    callback.onError(error.toString())
                }
                ClickToPayEvent.CANCEL -> {
                    callback.onClosed()
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            Log.d("WebViewBridge", "postMessage: $e")
        }
    }
}