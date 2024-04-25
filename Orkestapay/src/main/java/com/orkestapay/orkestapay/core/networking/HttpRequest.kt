package com.orkestapay.orkestapay.core.networking

import java.net.URL

internal data class HttpRequest(
    val url: URL,
    val method: HttpMethod = HttpMethod.GET,
    val body: String? = null,
    val headers: MutableMap<String, String> = mutableMapOf(),
)