package com.orkestapay.orkestapay

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.util.DisplayMetrics
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.orkestapay.orkestapay.client.OrkestapayClient
import com.orkestapay.orkestapay.client.apirequest.PaymentMethodListener
import com.orkestapay.orkestapay.client.apirequest.PromotionsListener
import com.orkestapay.orkestapay.client.enums.PaymentMethodType
import com.orkestapay.orkestapay.client.model.Card
import com.orkestapay.orkestapay.client.model.PaymentMethod
import com.orkestapay.orkestapay.client.model.PaymentMethodResponse
import com.orkestapay.orkestapay.client.model.PromotionsResponse
import com.orkestapay.orkestapay.core.devicesession.DeviceSessionListener
import com.orkestapay.orkestapay.core.devicesession.JsInterface
import com.orkestapay.orkestapay.core.networking.OrkestapayError
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class LibraryUnitTest {


    //@Mock
    //private var mockContext: Context = mockk<Context>(relaxed = true)
    //private val promotionsListener = mockk<PromotionsListener>(relaxed = true)
    //val context  =  Mockito.mock(Context::class.java)


    @Mock
    private val mockApplicationContext: Context? = null
    @Mock
    private val mockContextResources: Resources? = null
    @Mock
    private val mockDisplayMetrics: DisplayMetrics? = null
    @Mock
    private val mockApplicationInfo: ApplicationInfo? = null

    @Mock
    private val typedArray: TypedArray? = null


    @Before
    fun setupTests() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.openMocks(this)

        // During unit testing sometimes test fails because of your methods
        // are using the app Context to retrieve resources, but during unit test the Context is null
        // so we can mock it.
        `when`(mockApplicationContext!!.resources).thenReturn(mockContextResources)
        `when`(mockApplicationContext.resources.displayMetrics).thenReturn(mockDisplayMetrics)
        `when`(mockApplicationContext.applicationInfo).thenReturn(mockApplicationInfo)
        //`when`(mockApplicationContext.ty).thenReturn(mockApplicationInfo)

        //`when`(mockContextResources!!.typed(anyInt())).thenReturn(100f)
        `when`(mockContextResources!!.getIntArray(anyInt())).thenReturn(intArrayOf(1, 2, 3))
        `when`(mockApplicationContext.theme).thenReturn(Resources.getSystem().newTheme())
        `when`(mockApplicationContext.theme.obtainStyledAttributes(anyInt(), any())).thenReturn(typedArray)
        // here you can mock additional methods ...
        `when`(mockApplicationContext.applicationContext).thenReturn(mockApplicationContext)


        // if you have a custom Application class, you can inject the mock context like this
        //MyCustomApplication.setAppContext(mockApplicationContext)
    }

    @Test
    fun createDeviceSession() = runBlocking {
        //val orkestapay = OrkestapayClient("mch_591bbfb20c324605877afc9b01d715c3", "pk_test_c50ogjsxw0uhir2wc9kx0uxily0kzwj2", false)
        var responseOk = false
        var response: String? = null
        /*orkestapay.creteDeviceSession(RuntimeEnvironment.getApplication().baseContext, object : DeviceSessionListener {
            override fun onSuccess(deviceSession: String) {
                println(deviceSession)
                responseOk = true
                response = deviceSession
            }

            override fun onError(error: String) {
                println(error)
                responseOk = false
            }
        })*/
        //delay(15000)


        val urlString = "https://checkout.sand.orkestapay.com"
        val webView = WebView(mockApplicationContext!!).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JsInterface(object : DeviceSessionListener{
                override fun onSuccess(deviceSession: String) {
                    println(deviceSession)
                    responseOk = true
                    response = deviceSession
                }

                override fun onError(error: String) {
                    println(error)
                    responseOk = false
                }

            }), "androidListener")
            //loadUrl()
        }

        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                println("page finished")
            }
            @Suppress("deprecation")
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                println(description)
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                    view,
                    rerr.errorCode,
                    rerr.description.toString(),
                    req.url.toString()
                )
            }
        })

        webView.loadUrl("$urlString/script/device-session?merchant_id=mch_591bbfb20c324605877afc9b01d715c3&public_key=pk_test_c50ogjsxw0uhir2wc9kx0uxily0kzwj2")
        //org.awaitility.kotlin.await.atMost(20, TimeUnit.SECONDS).until { response != null }
        delay(15000)
        Assert.assertTrue(responseOk)
        Assert.assertTrue(response != null)
    }

    @Test
    fun createPayentMethod() = runBlocking  {
        val orkestapay = OrkestapayClient("mch_591bbfb20c324605877afc9b01d715c3", "pk_test_c50ogjsxw0uhir2wc9kx0uxily0kzwj2", false)
        val card = Card("4111111111111111", "12", "2025", "123", "Jhon Doe", true)
        val paymentMethod = PaymentMethod("test card",null, "c9c5229e-fed9-4ac7-b011-2bd129934f52", PaymentMethodType.CARD, card)

        var responseOk = false
        var response:PaymentMethodResponse? = null
        orkestapay.createPaymentMethod(paymentMethod, object : PaymentMethodListener {
            override fun onSuccess(paymentMethod: PaymentMethodResponse) {
                println(paymentMethod.toString())
                responseOk = true
                response = paymentMethod
            }

            override fun onError(error: OrkestapayError) {
                println(error.toString())
                responseOk = false
            }
        })
        delay(5000)
        Assert.assertTrue(responseOk)
        Assert.assertTrue(response != null)

    }

    @Test
    fun getPromotions() = runBlocking  {
        val orkestapay = OrkestapayClient("mch_591bbfb20c324605877afc9b01d715c3", "pk_test_c50ogjsxw0uhir2wc9kx0uxily0kzwj2", false)
        //orkestapay.getPromotions("477291", "MXN", "1000", promotionsListener)
        //val resultSlot = slot<List<PromotionsResponse>>()
        //verify(exactly = 1) {promotionsListener.onSuccess(capture(resultSlot))}

        var responseOk = false
        var response:List<PromotionsResponse> = listOf()
        orkestapay.getPromotions("477291", "MXN", "1000", object : PromotionsListener{
            override fun onSuccess(promotions: List<PromotionsResponse>) {
                println(promotions.toString())
                responseOk = true
                response = promotions
            }

            override fun onError(error: OrkestapayError) {
                println(error.toString())
                responseOk = false
            }
        })
        delay(5000)
        Assert.assertTrue(responseOk)
        Assert.assertTrue(response.isNotEmpty())

    }
}