package com.orkestapay.orkestapay.core.networking.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
@OptIn(InternalSerializationApi::class)
@Serializable
data class ErrorResponse(@JsonNames("request_id") val requestId: String, val category: String? = null,
                         val message: String? = null, val error: String? = null, @SerialName("validation_errors") val validationErrors:List<FieldError>? = null)
