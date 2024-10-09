package com.orkestapay.orkestapay.core.googlepay

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.orkestapay.orkestapay.client.apirequest.OrkestapayAPI
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodListener
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.googlepay.GooglePayData
import com.orkestapay.orkestapay.client.model.googlepay.PaymentMethodGooglePay
import com.orkestapay.orkestapay.core.clicktopay.WebviewActivity
import com.orkestapay.orkestapay.core.networking.CoreConfig
import com.orkestapay.orkestapay.core.networking.Environment
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


internal class GooglePayClient(private val coreConfig: CoreConfig) {
    private lateinit var paymentsClient: PaymentsClient
    private lateinit var callback: GooglePayCallback
    private lateinit var context: Context

    fun googlePaySetup(context: Context, callback: GooglePayCallback) {
        this.callback = callback
        this.context = context
        paymentsClient = createPaymentsClient(context)
        CoroutineScope(Dispatchers.IO).launch {
            possiblyShowGooglePayButton()
        }
    }

    suspend fun googlePayCheckout(googlePayData: GooglePayData, orkestapayAPI: OrkestapayAPI) {
        if (!this::callback.isInitialized) return
        GooglePayActivity.setListeners(callback, object: GooglePayAuthCallback {
            override fun onSuccess(paymentMethod: PaymentMethodGooglePay, activity: GooglePayActivity) {
                CoroutineScope(Dispatchers.IO).launch {
                    orkestapayAPI.createPaymentMethodGooglePay(paymentMethod, object: PaymentMethodListener {
                        override fun onSuccess(paymentMethod: PaymentMethodResponse) {
                            callback.onSuccess(paymentMethod)
                            activity.finish()
                        }

                        override fun onError(error: OrkestapayError) {
                            callback.onError(error.toString())
                        }

                    })
                }

            }

        })
        val intent = Intent(context, GooglePayActivity::class.java).apply {
            putExtra(GooglePayActivity.GOOGLE_PAY_DATA, googlePayData)
            putExtra(GooglePayActivity.IS_SANDBOX, isSandbox())
            putExtra(GooglePayActivity.MERCHANT_ID, coreConfig.merchantId)
            putExtra(GooglePayActivity.MERCHANT_NAME, "Merchant Test")
            putExtra(GooglePayActivity.GATEWAY, "orkestapay")
        }

        context.startActivity(intent)
    }

    private fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(if(isSandbox()) WalletConstants.ENVIRONMENT_TEST else WalletConstants.ENVIRONMENT_PRODUCTION)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private suspend fun fetchCanUseGooglePay(): Boolean {
        val request = IsReadyToPayRequest.fromJson(GooglePayUtil.isReadyToPayRequest().toString())
        return paymentsClient.isReadyToPay(request).await()
    }

    private suspend fun possiblyShowGooglePayButton() {
        val isReady = runCatching {
            fetchCanUseGooglePay()
        }.onFailure {
            it.message?.let { error -> callback.onError(error) }
        }.getOrDefault(false)

        callback.onReady(isReady)
    }

    private fun isSandbox(): Boolean {
        return coreConfig.environment == Environment.SANDBOX
    }

}