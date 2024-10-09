package com.orkestapay.orkestapay.core.googlepay

import android.content.Context
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

object GooglePayUtil {
    val CENTS = BigDecimal(100)

    private val baseRequest = JSONObject()
        .put("apiVersion", 2)
        .put("apiVersionMinor", 0)

    private fun gatewayTokenizationSpecification(gateway: String, merchantId: String): JSONObject =
        JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put("parameters", JSONObject(mapOf(
                "gateway" to gateway,
                "gatewayMerchantId" to merchantId
            )))

    private val allowedCardNetworks = JSONArray(GooglePayConstants.SUPPORTED_NETWORKS)

    private val allowedCardAuthMethods = JSONArray(GooglePayConstants.SUPPORTED_METHODS)

    private fun baseCardPaymentMethod(): JSONObject =
        JSONObject()
            .put("type", "CARD")
            .put(
                "parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
            )

    private fun cardPaymentMethod(gateway: String, merchantId: String): JSONObject = baseCardPaymentMethod()
        .put("tokenizationSpecification", gatewayTokenizationSpecification(gateway, merchantId))

    fun allowedPaymentMethods(gateway: String, merchantId: String): JSONArray = JSONArray().put(cardPaymentMethod(gateway, merchantId))

    fun isReadyToPayRequest(): JSONObject? =
        try {
            baseRequest
                .put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod()))
        } catch (e: JSONException) {
            null
        }


    private fun merchantInfo(merchantName: String): JSONObject =
        JSONObject().put("merchantName", merchantName)


    private fun getTransactionInfo(price: String, currencyCode: String, countryCode: String): JSONObject =
        JSONObject()
            .put("totalPrice", price)
            .put("totalPriceStatus", "FINAL")
            .put("totalPriceLabel", "TOTAL")
            .put("countryCode", countryCode)
            .put("currencyCode", currencyCode)

    fun getPaymentDataRequest(amount: String, currencyCode: String, countryCode: String, gateway: String, merchantId: String, merchantName: String): JSONObject =
        baseRequest
            .put("allowedPaymentMethods", allowedPaymentMethods(gateway, merchantId) )
            .put("transactionInfo", getTransactionInfo(amount, currencyCode, countryCode))
            .put("merchantInfo", merchantInfo(merchantName))
}

fun Long.centsToString() = BigDecimal(this)
    .divide(GooglePayUtil.CENTS)
    .setScale(2, RoundingMode.HALF_EVEN)
    .toString()