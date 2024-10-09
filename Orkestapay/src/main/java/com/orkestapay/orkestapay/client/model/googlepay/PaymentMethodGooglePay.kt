package com.orkestapay.orkestapay.client.model.googlepay

import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.BillingAddress
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodGooglePay(@SerialName("google_pay") val googlePay: PaymentMethodGooglePayDetails,
                                  val type: PaymentMethodType,
                                  @SerialName("customer_id") val customerId: String? = null,
                                  val alias: String? = null,
                                  @SerialName("billing_address") val billingAddress: BillingAddress? = null)
