package com.orkestapay.orkestapay.core.networking.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
@OptIn(InternalSerializationApi::class)
@Serializable
data class FieldError(val field: String, val message: String)
