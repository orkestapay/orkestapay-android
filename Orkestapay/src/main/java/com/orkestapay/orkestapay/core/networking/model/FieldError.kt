package com.orkestapay.orkestapay.core.networking.model

import kotlinx.serialization.Serializable

@Serializable
data class FieldError(val field: String, val message: String)
