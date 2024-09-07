package com.orkestapay.orkestapay.client

import android.content.Context
import android.util.Log
import com.orkestapay.orkestapay.client.apirequest.OrkestapayAPI
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodListener
import com.orkestapay.orkestapay.client.apirequest.PromotionsListener
import com.orkestapay.orkestapay.client.model.PaymentMethod
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayClient
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionClient
import com.orkestapay.orkestapay.core.networking.CoreConfig
import com.orkestapay.orkestapay.core.networking.Environment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrkestapayClient(merchantId: String, publicKey: String, isProductionMode: Boolean ) {
    private var coreConfig: CoreConfig
    private var deviceSessionClient: DeviceSessionClient
    private var clickToPayClient: ClickToPayClient
    private var orkestapayAPI: OrkestapayAPI

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
    }

    fun creteDeviceSession(context: Context, callback: DeviceSessionListener){
        deviceSessionClient.getDeviceSessionId(context, callback)
    }

    fun createPaymentMethod(paymentMethod: PaymentMethod, listener: PaymentMethodListener){
        CoroutineScope(Dispatchers.IO).launch {
            orkestapayAPI.createPaymentMethod(paymentMethod, listener)
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
}