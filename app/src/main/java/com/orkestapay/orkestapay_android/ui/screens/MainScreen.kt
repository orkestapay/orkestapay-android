package com.orkestapay.orkestapay_android.ui.screens

import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.orkestapay.orkestapay.client.OrkestapayClient
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.clicktopay.ClickToPay
import com.orkestapay.orkestapay.core.clicktopay.ClickToPayCallback
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.googlepay.GooglePayCallback
import com.orkestapay.orkestapay_android.R
import com.orkestapay.orkestapay_android.data.repository.PaymentRepositoryImpl
import com.orkestapay.orkestapay_android.navigation.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    val ctx = LocalContext.current
    //uat click to pay
    //val orkestapay = OrkestapayClient("mch_38c8cb7eeb054c6f90eac00d71542e5f", "pk_test_zls6cvk02ppsjqnqj2cm0tiwewrn4d5f", false)
    //sand click to pay
    //val orkestapay = OrkestapayClient("mch_89d235df1c944396808a73564883cde7", "pk_test_s2jtj1klwwmijfj1vyuvsult466hj5n5", false)


    //val orkestapay = OrkestapayClient("mch_d58cbba060ac411289c160f336c8c41b", "pk_test_h2gn9ksk8q7mfhovweviyjp93b7zcgus", false)
    //var orkestapay = OrkestapayClient("mch_e33f6f87ec5b47d1a41519f4ed3fcf53", "pk_test_vywzkgf0im78h6fpdr22nx322x98ae2z", false)
    val orkestapay = OrkestapayClient("mch_591bbfb20c324605877afc9b01d715c3", "pk_test_c50ogjsxw0uhir2wc9kx0uxily0kzwj2",  false)
    var deviceSessionId by remember { mutableStateOf("") }
    var googlePaymentMethod by remember { mutableStateOf("") }
    var click2PayPaymentMethod by remember { mutableStateOf("") }
    var btnGoogleIsVisible by remember { mutableStateOf(false) }
    val frameLayout = FrameLayout(ctx).apply {
        layoutParams = ViewGroup.LayoutParams(1, 1)
        visibility = View.INVISIBLE
    }

    var amountText by remember { mutableStateOf("500") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf(PaymentMethodType.CLICK_TO_PAY) }

    val isFormValid by remember {
        derivedStateOf {
            val amountValue = amountText.toDoubleOrNull() ?: 0.0
            amountValue > 0.0 && name.isNotBlank() && lastName.isNotBlank() && phone.length == 10 && Patterns.EMAIL_ADDRESS.matcher(email).matches() && paymentMethod == PaymentMethodType.CLICK_TO_PAY
        }
    }

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
            Log.d("onError google pay", error)
        }

    })

    LaunchedEffect(Unit){
        orkestapay.creteDeviceSession(ctx, frameLayout, object : DeviceSessionListener{
            override fun onSuccess(deviceSession: String) {
                Log.d("device_session_id", deviceSession)
                deviceSessionId = deviceSession
            }

            override fun onError(error: String) {
                Log.d("error", error)
            }
        })
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(rememberScrollState())
        .padding(17.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        OutlinedTextField(amountText,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                    amountText = newValue
                }},
            label = { Text("Monto") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        OutlinedTextField(phone,
            onValueChange = { newText ->
                if (newText.length <= 10) {
                    phone = newText
                } },
            label = { Text("Número telefónico") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            )
        )

        Column(
            modifier = Modifier.padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text("Selecciona tu método de pago", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(2.dp, if (paymentMethod == PaymentMethodType.CLICK_TO_PAY) Color.Black else Color.LightGray, shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        paymentMethod = PaymentMethodType.CLICK_TO_PAY
                    }

            ) {
                Image(
                    painter = painterResource(R.drawable.click_to_pay),
                    contentDescription = null
                )
                Text("Credit/Debit")
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.mastercard),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(R.drawable.visa),
                    contentDescription = null
                )
                Image(
                    painter = painterResource(R.drawable.amex),
                    contentDescription = null
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(2.dp, if (paymentMethod == PaymentMethodType.CARD) Color.Black else Color.LightGray, shape = RoundedCornerShape(6.dp))
                    .padding(13.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        paymentMethod = PaymentMethodType.CARD
                    }

            ) {
                Box(
                    modifier = Modifier
                        .size(width = 35.dp, height = 25.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
                Text("Payment method A")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(2.dp, if (paymentMethod == PaymentMethodType.GOOGLE_PAY) Color.Black else Color.LightGray, shape = RoundedCornerShape(6.dp))
                    .padding(13.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        paymentMethod = PaymentMethodType.GOOGLE_PAY
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 35.dp, height = 25.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )
                Text("Payment method B")
            }
        }

        Button(onClick = {
            val clickToPay = ClickToPay(email, name, lastName, "52", phone, amountText, "MXN")
            orkestapay.clickToPayCheckout(ctx, clickToPay, object : ClickToPayCallback{
                override fun onSuccess(paymentMethod: PaymentMethodResponse) {
                    Log.d("onSuccess", paymentMethod.toString())
                    click2PayPaymentMethod = paymentMethod.paymentMethodId
                    navController.navigate("${Screen.Detail.route}/$amountText/$email/$click2PayPaymentMethod/$deviceSessionId")
                }

                override fun onClosed() {
                    Log.d("onClosed", "closed")
                }

                override fun onError(error: String) {
                    Log.d("onError", error)
                }
            })
        }, modifier = Modifier.fillMaxWidth()
            .padding(top = 10.dp),
            enabled = isFormValid
        ) {
            Text("Continuar")
        }

        /*
        Button(onClick = {
            orkestapay.creteDeviceSession(ctx, frameLayout, object : DeviceSessionListener{
                override fun onSuccess(deviceSession: String) {
                    Log.d("device_session_id", deviceSession)
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
                    val googlePayData = GooglePayData("2836.02", "MXN", "MX", true)
                    orkestapay.googlePayCheckout(googlePayData)
                },
                allowedPaymentMethods = GooglePayUtil.allowedPaymentMethods(orkestapay.googlePaymentMethodData!!.properties.gateway, orkestapay.googlePaymentMethodData!!.properties.merchantId).toString()
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(googlePaymentMethod)

        Spacer(Modifier.height(20.dp))

        Button(onClick = {
            val clickToPay = ClickToPay("orkestapay.user15@yopmail.com", "John", "Doe", "52", "4411223344", "100", "MXN")
            orkestapay.clickToPayCheckout(ctx, clickToPay, object : ClickToPayCallback{
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

        Spacer(Modifier.height(20.dp))*/
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    //MainScreen()
}
