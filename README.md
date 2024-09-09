# Orkestapay Android 

Orkestapay Android create payment methods and get promotions

## Installation

#### Minimum Requirements

- Android SDK 24+

### Adding It To Your Project

build.gradle
```groovy
dependencies {
  implementation 'com.orkestapay:orkestapay:0.0.6'
}
```

build.gradle.kts
```groovy
dependencies {
  implementation("com.orkestapay:orkestapay:0.0.6")
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
orkestapay.creteDeviceSession(context, object : DeviceSessionListener{
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

Library contains a function to open checkout to Click to Pay

```groovy
val clickToPay = ClickToPay("dd1cbff5-dc54-4665-a449-554d20b61c0a_dpa0","en_US","Testdpa0",
    listOf("mastercard","visa","amex"),"orkestapay.customer.02@yopmail.com", "John", "Doe", "+52","7712345678")
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
```
