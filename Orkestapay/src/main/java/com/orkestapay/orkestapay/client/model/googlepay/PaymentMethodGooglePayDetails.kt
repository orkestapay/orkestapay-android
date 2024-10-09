package com.orkestapay.orkestapay.client.model.googlepay

import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodGooglePayDetails(val type: PaymentMethodType? = null,
                                         @SerialName("wallet_type") val walletType: PaymentMethodType? = null,
                                         @SerialName("api_version_minor") override val apiVersionMinor: Int,
                                         @SerialName("api_version") override val apiVersion: Int,
                                         @SerialName("payment_method_data") override val paymentMethodData: PaymentMethodDataGooglePay
): GooglePayPaymentData()
