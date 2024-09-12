package com.orkestapay.orkestapay.core.googlepay

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.button.PayButton
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

class GooglePayActivity : ComponentActivity() {
    private lateinit var paymentsClient: PaymentsClient
    val callback: GooglePayCallback?
        get() {
            return listener
        }
    companion object {

        private var listener: GooglePayCallback? = null
        fun setListener(callback: GooglePayCallback) {
            listener = callback
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentsClient = createPaymentsClient(this)

        /*googlePayButton.initialize(
            ButtonOptions.newBuilder()
                .setAllowedPaymentMethods(GooglePayUtil.allowedPaymentMethods.toString()).build()
        )*/
        //googlePayButton.setOnClickListener { requestPayment() }
        //requestPayment()

        CoroutineScope(Dispatchers.IO).launch {
            possiblyShowGooglePayButton()
        }
    }

    private fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private fun requestPayment() {
        // The price provided to the API should include taxes and shipping.
        val task: Task<PaymentData> = getLoadPaymentDataTask(1000L)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }

    private fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData> {
        val paymentDataRequestJson: JSONObject = GooglePayUtil.getPaymentDataRequest(priceCents)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        return paymentsClient.loadPaymentData(request)
    }

    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                val result = taskResult.result
                if (result != null) {
                    val paymentDataJson = JSONObject(result.toJson())
                    //val googlePayResult = GooglePayResult.fromJson(paymentDataJson)


                    callback?.onSuccess(paymentDataJson.toString())
                } else {
                    callback?.onError("Google Pay missing result data.")
                }
            }
            CommonStatusCodes.CANCELED -> {
                callback?.onCancel()
                finish()
            }

            else -> {
                callback?.onError("Google Pay failed with error ${taskResult.status.statusCode}: ${taskResult.status.statusMessage.orEmpty()}")
            }
        }
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

        callback?.onReady(isReady)
        //setGooglePayAvailable(isReady)
    }
}