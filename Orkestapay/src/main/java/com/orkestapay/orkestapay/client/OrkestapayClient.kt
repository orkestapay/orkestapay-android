package com.orkestapay.orkestapay.client

import android.content.Context
import android.util.Log
import com.orkestapay.orkestapay.client.apirequest.OrkestapayAPI
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodDataListener
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodListener
import com.orkestapay.orkestapay.client.apirequest.PromotionsListener
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.PaymentMethod
import com.orkestapay.orkestapay.client.model.PaymentMethodData
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.client.model.googlepay.GooglePayData
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayClient
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionClient
import com.orkestapay.orkestapay.core.googlepay.GooglePayCallback
import com.orkestapay.orkestapay.core.googlepay.GooglePayClient
import com.orkestapay.orkestapay.core.networking.CoreConfig
import com.orkestapay.orkestapay.core.networking.Environment
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrkestapayClient(merchantId: String, publicKey: String, isProductionMode: Boolean ) {
    private var coreConfig: CoreConfig
    private var deviceSessionClient: DeviceSessionClient
    private var clickToPayClient: ClickToPayClient
    private var googlePayClient: GooglePayClient
    private var orkestapayAPI: OrkestapayAPI
    lateinit var googlePaymentMethodData: PaymentMethodData

    init{
        var id = merchantId
        if (!merchantId.startsWith( "mid_")) {
            id = "mid_$id"
        }
        coreConfig = CoreConfig(id, publicKey, when {
            isProductionMode -> Environment.PRODUCTION
            else -> Environment.SANDBOX
        })
        deviceSessionClient = DeviceSessionClient(coreConfig)
        clickToPayClient = ClickToPayClient(coreConfig)
        orkestapayAPI = OrkestapayAPI(coreConfig)
        googlePayClient = GooglePayClient(coreConfig)
    }

    fun creteDeviceSession(context: Context, callback: DeviceSessionListener){
        deviceSessionClient.getDeviceSessionId(context, callback)
    }

    fun createPaymentMethod(paymentMethod: PaymentMethod, listener: PaymentMethodListener){
        CoroutineScope(Dispatchers.IO).launch {
            orkestapayAPI.createPaymentMethodCard(paymentMethod, listener)
        }
    }

    fun getPromotions(binNumber: String, currency: String, totalAmount: String, listener: PromotionsListener){
        CoroutineScope(Dispatchers.IO).launch {
            orkestapayAPI.getPromotions(binNumber, currency, totalAmount, listener)
        }
    }

    fun clickToPayCheckout(context: Context, clickToPay: ClickToPay, listener: ClickToPayListener){
        clickToPayClient.openClickToPayCheckout(context, clickToPay, listener)
    }

    fun googlePaySetup(context: Context, callback: GooglePayCallback){
       CoroutineScope(Dispatchers.IO).launch {
           orkestapayAPI.getPaymentMethodInfo(PaymentMethodType.GOOGLE_PAY, object : PaymentMethodDataListener {
               override fun onSuccess(data: PaymentMethodData) {
                   googlePaymentMethodData = data
                   googlePayClient.googlePaySetup(context, callback)
               }

               override fun onError(error: OrkestapayError) {
                   callback.onError(error.toString())
               }
           })
       }
    }

    fun googlePayCheckout(googlePayData: GooglePayData){
        CoroutineScope(Dispatchers.IO).launch {
            googlePayClient.googlePayCheckout(googlePayData, orkestapayAPI)
        }
    }

}