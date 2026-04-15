package com.orkestapay.orkestapay_android.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orkestapay.orkestapay_android.domain.model.Resource

@Composable
fun DetailScreen(viewModel: PaymentViewModel, amount: String, email: String, paymentMethodId: String, deviceSession: String) {
    val state = viewModel.state

    LaunchedEffect(Unit) { viewModel.loadProducts(amount, email, paymentMethodId, deviceSession) }

    Column(
        modifier = Modifier.padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is Resource.Success -> {
                Text(state.data.paymentId)
            }
            is Resource.Error -> {
                Text(state.errorData?.message ?: "")
            }
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                )
            }
            null -> { /* Estado inicial, mostrar el formulario */ }
        }
    }

}
