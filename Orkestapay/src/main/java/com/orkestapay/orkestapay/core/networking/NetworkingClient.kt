package com.orkestapay.orkestapay.core.networking

import java.net.URL
import java.util.Locale

internal class NetworkingClient internal constructor(
    private val config: CoreConfig,
    private val http: OrkestaHttp = OrkestaHttp(),
    private val language: String = Locale.getDefault().language
) {

    constructor(configuration: CoreConfig) : this(configuration, OrkestaHttp())

    suspend fun send(apiRequest: RestRequest): HttpResponse {
        val httpRequest = createHttpRequestFromAPIRequest(apiRequest, config)
        return http.send(httpRequest)
    }

    private fun createHttpRequestFromAPIRequest(
        apiRequest: RestRequest,
        configuration: CoreConfig,
    ): HttpRequest {
        val path = apiRequest.path
        val baseUrl = configuration.environment.url

        val url = URL("$baseUrl$path")
        val method = apiRequest.method
        val body = apiRequest.body

        val headers: MutableMap<String, String> = mutableMapOf()

        val credentials = "${configuration.merchantId}:${configuration.publicKey}"
        headers["Authorization"] = "Basic ${credentials.base64encoded()}"

        if (method == HttpMethod.POST) {
            headers["Content-Type"] = "application/json"
        }
        return HttpRequest(url, method, body, headers)
    }
}