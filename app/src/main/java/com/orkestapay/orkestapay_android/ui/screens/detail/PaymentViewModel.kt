package com.orkestapay.orkestapay_android.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orkestapay.orkestapay_android.domain.model.PaymentRequest
import com.orkestapay.orkestapay_android.domain.model.Resource
import com.orkestapay.orkestapay_android.domain.model.SuccessResponse
import com.orkestapay.orkestapay_android.domain.repository.PaymentRepository
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {
    var state by mutableStateOf<Resource<SuccessResponse>?>(null)

    fun loadProducts(amount: String, email: String, paymentMethodId: String, deviceSession: String) {
        viewModelScope.launch {
            state = Resource.Loading(true)
            val paymentRequest = PaymentRequest(amount.toDoubleOrNull() ?: 0.0, email, paymentMethodId, deviceSession)
            state = repository.payment(paymentRequest)
        }
    }
}