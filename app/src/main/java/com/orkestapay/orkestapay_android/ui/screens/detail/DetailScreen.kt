package com.orkestapay.orkestapay_android.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orkestapay.orkestapay_android.R
import com.orkestapay.orkestapay_android.data.repository.PaymentRepositoryImpl
import com.orkestapay.orkestapay_android.di.NetworkModule
import com.orkestapay.orkestapay_android.domain.model.Card
import com.orkestapay.orkestapay_android.domain.model.Customer
import com.orkestapay.orkestapay_android.domain.model.ErrorResponse
import com.orkestapay.orkestapay_android.domain.model.Resource
import com.orkestapay.orkestapay_android.domain.model.SuccessResponse

@Composable
fun DetailScreen(viewModel: PaymentViewModel, amount: String, email: String, paymentMethodId: String, deviceSession: String) {
    val state = viewModel.state

    LaunchedEffect(Unit) { viewModel.loadProducts(amount, email, paymentMethodId, deviceSession) }

    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (state) {
            is Resource.Success -> {
                Image(
                    painter = painterResource(R.drawable.success_icon),
                    modifier = Modifier.size(100.dp),
                    contentDescription = null
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {

                    Column {
                        Text("Order Id:", fontWeight = FontWeight.SemiBold)
                        Text(state.data.orderId)

                    }

                    Column {
                        Text("Payment Id:", fontWeight = FontWeight.SemiBold)
                        Text(state.data.paymentId)

                    }

                    Column {
                        Text("Total amount:", fontWeight = FontWeight.SemiBold)
                        Text("$${state.data.totalAmount}")
                    }

                    Column() {
                        Text("Contact information", fontWeight = FontWeight.SemiBold)
                        Text(state.data.customer.email)
                    }

                    Column() {
                        Text("Payment", fontWeight = FontWeight.SemiBold)
                        Text(state.data.card.issuerBankName)
                        Text("**** ${state.data.card.lastFour}")
                    }
                }

            }
            is Resource.Error -> {
                Image(
                    painter = painterResource(R.drawable.error_icon),
                    modifier = Modifier.size(100.dp),
                    contentDescription = null
                )
                Text(state.errorData?.message ?: "")
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(50.dp)
                        .size(100.dp)
                )
            }
            null -> { }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    val viewModel = PaymentViewModel(PaymentRepositoryImpl(NetworkModule.api))
    val response = SuccessResponse(
        paymentId = "adfsdfasd",
        orderId = "sfsdfsdfa",
        merchantOrderId = "sdfsdfsdf",
        currency = "MXN",
        totalAmount = "500",
        status = "",
        customer = Customer(email = "juan@gmail.com"),
        card = Card(
            bin = "",
            lastFour = "1234",
            brand = "",
            cardType = "",
            holderName = "",
            holderLastName = "",
            expirationMonth = "",
            expirationYear = "",
            issuerBankName = "sdfsdf",
            oneTimeUse = true
        ),
        createdAt = ""
    )
    val errorResponse = ErrorResponse(
        step = "",
        requestId = "",
        category = "",
        message = "Error desconocido",
        timestamp = "",
        code = ""
    )
    viewModel.state = Resource.Success(response)
    DetailScreen(viewModel, "500", "he@gmail.com", "adfsdf", "adfadfsd")
}