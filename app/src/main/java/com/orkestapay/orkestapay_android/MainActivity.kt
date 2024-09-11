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
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayListener
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import com.orkestapay.orkestapay_android.ui.theme.OrkestapayandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrkestapayandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
    val orkestapay = OrkestapayClient("mch_38c8cb7eeb054c6f90eac00d71542e5f", "pk_test_zls6cvk02ppsjqnqj2cm0tiwewrn4d5f", false)
    val ctx = LocalContext.current
    var deviceSessionId by remember { mutableStateOf("") }
    var btnGoogleIsVisible by remember { mutableStateOf(false) }

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

            Spacer(Modifier.height(16.dp))

            if(btnGoogleIsVisible) {
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
                    Text(text = "Google Play")
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(onClick = {
                val clickToPay = ClickToPay("orkestapay.customer2@yopmail.com", "John", "Doe", "52", "4411223344", true)
                orkestapay.clickToPayCheckout(ctx, clickToPay, object : ClickToPayListener{
                    override fun onSuccess(payment: String) {
                       Log.d("onSuccess", payment)
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
        }
    }
}


