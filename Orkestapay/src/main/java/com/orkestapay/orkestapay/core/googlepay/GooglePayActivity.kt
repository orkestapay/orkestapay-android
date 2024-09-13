package com.orkestapay.orkestapay.core.googlepay

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.googlepay.GooglePayData
import com.orkestapay.orkestapay.client.model.googlepay.GooglePayDataResult
import com.orkestapay.orkestapay.client.model.googlepay.PaymentMethodGooglePay
import com.orkestapay.orkestapay.client.model.googlepay.PaymentMethodGooglePayDetails
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.Serializable

class GooglePayActivity : ComponentActivity() {
    lateinit var paymentsClient: PaymentsClient
    private lateinit var googlePayData: GooglePayData
    private var isSandbox: Boolean = true
    private lateinit var merchantId: String
    private lateinit var gateway: String
    private lateinit var merchantName: String

    val callback: GooglePayCallback?
        get() {
            return listener
        }

    val googlePayAuthCallback: GooglePayAuthCallback?
        get() {
            return listenerAuth
        }
    companion object {
        const val GOOGLE_PAY_DATA = "GOOGLE_PAY_DATA"
        const val IS_SANDBOX = "IS_SANDBOX"
        const val MERCHANT_ID = "MERCHANT_ID"
        const val MERCHANT_NAME = "MERCHANT_NAME"
        const val GATEWAY = "GATEWAY"

        private var listener: GooglePayCallback? = null
        private var listenerAuth: GooglePayAuthCallback? = null
        fun setListeners(callback: GooglePayCallback, callbackAuth: GooglePayAuthCallback) {
            listener = callback
            listenerAuth = callbackAuth
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentsClient = createPaymentsClient(this)
        googlePayData = getSerializable(this, GOOGLE_PAY_DATA, GooglePayData::class.java)
        isSandbox = intent.getBooleanExtra(IS_SANDBOX, true)
        merchantId = intent.getStringExtra(MERCHANT_ID)!!
        merchantName = intent.getStringExtra(MERCHANT_NAME)!!
        gateway = intent.getStringExtra(GATEWAY)!!
        requestPayment()

    }

    private fun createPaymentsClient(context: Context): PaymentsClient {
        Log.d("isSandbox", isSandbox.toString())
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(if(isSandbox) WalletConstants.ENVIRONMENT_TEST else WalletConstants.ENVIRONMENT_PRODUCTION)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private fun requestPayment() {
        // The price provided to the API should include taxes and shipping.
        val task: Task<PaymentData> = getLoadPaymentDataTask(googlePayData.amount, googlePayData.currencyCode, googlePayData.countryCode)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }

    private fun getLoadPaymentDataTask(amount: String, currencyCode: String, countryCode: String): Task<PaymentData> {
        val paymentDataRequestJson: JSONObject = GooglePayUtil.getPaymentDataRequest(amount, currencyCode, countryCode, gateway, merchantId, merchantName)
        Log.d("paymentDataRequestJson", paymentDataRequestJson.toString())
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        return paymentsClient.loadPaymentData(request)
    }

    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                val result = taskResult.result
                if (result != null) {
                    val paymentDataJson = JSONObject(result.toJson())
                    val json = Json { ignoreUnknownKeys= true }
                    val googlePayData = json.decodeFromString<GooglePayDataResult>(paymentDataJson.toString())

                    val paymentMethodGooglePayDetails = PaymentMethodGooglePayDetails(PaymentMethodType.GOOGLE_PAY, PaymentMethodType.GOOGLE_PAY, googlePayData.apiVersionMinor, googlePayData.apiVersion, googlePayData.paymentMethodData)
                    val paymentMethodGooglePay = PaymentMethodGooglePay(paymentMethodGooglePayDetails, PaymentMethodType.GOOGLE_PAY)
                    googlePayAuthCallback?.onSuccess(paymentMethodGooglePay, this)
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

    @Suppress("UNCHECKED_CAST")
    private fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            activity.intent.getSerializableExtra(name, clazz)!!
        else
            activity.intent.getSerializableExtra(name) as T
    }


}