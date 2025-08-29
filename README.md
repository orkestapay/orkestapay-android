# Orkestapay Android 

Orkestapay Android create payment methods and get promotions

## Installation

#### Minimum Requirements

- Android SDK 24+

### Adding It To Your Project

build.gradle
```groovy
dependencies {
  implementation 'com.orkestapay:orkestapay:0.1.1'
}
```

build.gradle.kts
```groovy
dependencies {
  implementation("com.orkestapay:orkestapay:0.1.1")
}
```

# Usage

#### Create a instance 

For create an instance Orkestapay client needs:
- Merchant Id
- Public Key

```groovy
val orkestapay = OrkestapayClient("MERCHANT_ID", "PUBLIC_KEY", false)

```

If you want to switch to production mode, you must send isProductionMode as true


#### Create a Device Session Id

Library contains a function to generate device session id

```groovy
orkestapay.creteDeviceSession(context, layout, object : DeviceSessionListener{
    override fun onSuccess(deviceSession: String) {
        Log.d("dev session id", deviceSession)
    }

    override fun onError(error: String) {
        Log.d("error", error)
    }
})
```

#### Create Payment Method

Library contains a function to create a payment method

```groovy
val card = Card("4111111111111111", "12", "2025", "123", "Juan Pérez", true)
val paymentMethod = PaymentMethod("test card",null, deviceSessionId, PaymentMethodType.CARD, card)
orkestapay.createPaymentMethod(paymentMethod, object : PaymentMethodListener {
    override fun onSuccess(paymentMethod: PaymentMethodResponse) {
        Log.d("response", paymentMethod.toString())
    }

    override fun onError(error: OrkestapayError) {
        Log.e("error", error.toString())
    }
})
```

#### Get promotions

Library contains a function to get promotions

```groovy
orkestapay.getPromotions("123456", "MXN", "1000", object : PromotionsListener{
    override fun onSuccess(promotions: List<PromotionsResponse>) {
        Log.d("response", promotions.toString())
    }

    override fun onError(error: OrkestapayError) {
        Log.e("error", error.toString())
    }
})
```

#### Click to Pay Checkout

Library contains a function to open checkout to Click to Pay.

```groovy
val clickToPay = ClickToPay("customer@mail.com", "John", "Doe", "52", "4411223344", "100", "MXN", true, true)
val style = ClickToPayStyle("Click to Pay", Color.parseColor("#e87600"))

orkestapay.clickToPayCheckout(ctx, clickToPay, style, object : ClickToPayListener{
    override fun onSuccess(paymentMethod: String) {
       Log.d("onSuccess", paymentMethod)
    }

    override fun onClosed() {
        Log.d("onClosed", "closed")
    }

    override fun onError(error: String) {
        Log.d("onError", error)
    }

})
```


#### Google Pay

Library contains a function to create payment method through Google Pay.

##### Setup your integration
To use Google Pay, first enable the Google Pay API by adding the following to the <application> tag of your AndroidManifest.xml:

```groovy
<application>
  ...
  <meta-data
    android:name="com.google.android.gms.wallet.api.enabled"
    android:value="true" />
</application>
```

Check that Google Pay is available:

Implement GooglePayCallback and if the boolean isReady is true then the Google Pay button can be displayed

```groovy
orkestapay.googlePaySetup(ctx, object : GooglePayCallback{
      override fun onReady(isReady: Boolean) {
          Log.d("onReady", isReady.toString())
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
```

##### Add Google Pay Button
Google Pay button dependency

```groovy
dependencies {
  implementation("com.google.pay.button:compose-pay-button:PAY_BUTTON_VERSION")
}
```

Configure the button

```groovy
@Composable
fun GooglePayButton() {
   PayButton(
       onClick = {
          //Launch Google Pay modal
          val googlePayData = GooglePayData("1000", "MXN", "MX", true)
          orkestapay.googlePayCheckout(googlePayData)
       },
       allowedPaymentMethods = GooglePayUtil.allowedPaymentMethods(orkestapay.googlePaymentMethodData!!.properties.gateway, orkestapay.googlePaymentMethodData!!.properties.merchantId).toString()
   )
}
```
