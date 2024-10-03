package com.orkestapay.orkestapay_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orkestapay.orkestapay.client.OrkestapayClient
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodListener
import com.orkestapay.orkestapay.client.apirequest.PromotionsListener
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.Card
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.client.model.PaymentMethod
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.PromotionsResponse
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPayStyle
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.googlepay.GooglePayUtil
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import com.orkestapay.orkestapay_android.ui.theme.OrkestapayandroidTheme
import com.google.pay.button.PayButton
import com.orkestapay.orkestapay.client.model.googlepay.GooglePayData
import com.orkestapay.orkestapay.core.googlepay.GooglePayCallback

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestapayandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFFFF)
                ) {
                    Buttons()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Buttons() {
    //uat click to pay

    val orkestapay = OrkestapayClient("mch_38c8cb7eeb054c6f90eac00d71542e5f", "pk_test_zls6cvk02ppsjqnqj2cm0tiwewrn4d5f", false)
    //var orkestapay = OrkestapayClient("mch_e33f6f87ec5b47d1a41519f4ed3fcf53", "pk_test_vywzkgf0im78h6fpdr22nx322x98ae2z", false)
    val ctx = LocalContext.current
    var deviceSessionId by remember { mutableStateOf("") }
    var googlePaymentMethod by remember { mutableStateOf("") }
    var click2PayPaymentMethod by remember { mutableStateOf("") }
    var btnGoogleIsVisible by remember { mutableStateOf(false) }

    orkestapay.googlePaySetup(ctx, object : GooglePayCallback{
        override fun onReady(isReady: Boolean) {
            Log.d("onReady", isReady.toString())
            btnGoogleIsVisible = isReady
        }

        override fun onSuccess(paymentMethod: PaymentMethodResponse) {
            Log.d("onSuccess", paymentMethod.toString())
            googlePaymentMethod = paymentMethod.paymentMethodId
        }

        override fun onCancel() {
            Log.d("onCancel", "onCancel")
        }

        override fun onError(error: String) {
            Log.d("onError", error)
        }

    })

    OrkestapayandroidTheme {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            verticalArrangement =  Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Button(onClick = {
                orkestapay.creteDeviceSession(ctx, object : DeviceSessionListener{
                    override fun onSuccess(deviceSession: String) {
                        Log.d("dev session id", deviceSession)
                        deviceSessionId = deviceSession
                    }

                    override fun onError(error: String) {
                        Log.d("error", error)
                    }
                })

            }) {
                Text(text = "Get Session Id")
            }

            Spacer(Modifier.height(16.dp))

            Text(deviceSessionId)

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                val card = Card("4111111111111111", "12", "2025", "123", "Héctor Rdz", true)
                //val billingAddress = BillingAddress(null, null, null, null, null, "Av. Tecnológico 123", null, "Querétaro", "Querétaro", "MX", "76127")
                val paymentMethod = PaymentMethod("test card",null, deviceSessionId, PaymentMethodType.CARD, card)
                orkestapay.createPaymentMethod(paymentMethod, object : PaymentMethodListener {
                    override fun onSuccess(paymentMethod: PaymentMethodResponse) {
                        Log.d("response", paymentMethod.toString())
                    }

                    override fun onError(error: OrkestapayError) {
                        Log.e("error", error.toString())
                    }
                })
            }) {
                Text(text = "Create payment method")
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                orkestapay.getPromotions("477291", "MXN", "1000", object : PromotionsListener{
                    override fun onSuccess(promotions: List<PromotionsResponse>) {
                        Log.d("response", promotions.toString())
                    }

                    override fun onError(error: OrkestapayError) {
                        Log.e("error", error.toString())
                    }
                })
            }) {
                Text(text = "Get promotions")
            }

            Spacer(Modifier.height(25.dp))

            if(btnGoogleIsVisible) {
                PayButton(
                    modifier = Modifier
                        .fillMaxWidth().padding(16.dp, 10.dp),

                    onClick = {
                        val googlePayData = GooglePayData("5", "MXN", "MX", true)
                        orkestapay.googlePayCheckout(googlePayData)
                    },
                    allowedPaymentMethods = GooglePayUtil.allowedPaymentMethods(orkestapay.googlePaymentMethodData!!.properties.gateway, orkestapay.googlePaymentMethodData!!.properties.merchantId).toString()
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(googlePaymentMethod)

            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                val clickToPay = ClickToPay("orkestapay.user15@yopmail.com", "John", "Doe", "52", "4411223344", true, true)
                val style = ClickToPayStyle("Click to Pay", android.graphics.Color.parseColor("#e87600"))
                orkestapay.clickToPayCheckout(ctx, clickToPay, style, object : ClickToPayListener{
                    override fun onSuccess(paymentMethod: PaymentMethodResponse) {
                       Log.d("onSuccess", paymentMethod.toString())
                        click2PayPaymentMethod = paymentMethod.paymentMethodId
                    }

                    override fun onClosed() {
                        Log.d("onClosed", "closed")
                    }

                    override fun onError(error: String) {
                        Log.d("onError", error)
                    }

                })
            }) {
                Text(text = "Click To Pay")
            }

            Spacer(Modifier.height(16.dp))

            Text(click2PayPaymentMethod)

            Spacer(Modifier.height(20.dp))
        }
    }
}


