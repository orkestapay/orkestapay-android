package tech.tcpip.orkestapay_android.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.tcpip.orkestapay_android.data.remote.PaymentsApi
import tech.tcpip.orkestapay_android.data.repository.PaymentRepositoryImpl
import tech.tcpip.orkestapay_android.domain.repository.PaymentRepository
import java.util.concurrent.TimeUnit

object NetworkModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: PaymentsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://checkout.sbox.orkestapay.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentsApi::class.java)
    }

    val repository: PaymentRepository by lazy {
        PaymentRepositoryImpl(api)
    }
}