package com.orkestapay.orkestapay.core.networking

internal data class RestRequest(val path: String, val method: HttpMethod, val body: String? = null)