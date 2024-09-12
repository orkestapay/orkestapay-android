package com.orkestapay.orkestapay.core.googlepay
import android.content.Context
import android.content.Intent
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.orkestapay.orkestapay.core.clicktopay.WebviewActivity
import com.orkestapay.orkestapay.core.networking.CoreConfig
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

    fun googlePayCheckout() {
        GooglePayActivity.setListener(callback)
        val intent = Intent(context, GooglePayActivity::class.java)
        context.startActivity(intent)
    }

    private fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
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
        }.getOrDefault(false)

        callback.onReady(isReady)
        //setGooglePayAvailable(isReady)
    }


}