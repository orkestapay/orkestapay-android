package com.orkestapay.orkestapay.core.googlepay

import com.google.android.gms.wallet.WalletConstants

object GooglePayConstants {

    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

    val SUPPORTED_NETWORKS = listOf(
        "AMEX",
        "DISCOVER",
        "JCB",
        "MASTERCARD",
        "VISA")

    val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS")

    const val COUNTRY_CODE = "US"

    const val CURRENCY_CODE = "USD"

    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "orkestapay"

    val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "gatewayMerchantId" to "mch_5ded0b1bc6cf4abca17fdb6a37e30853"
    )

}