package com.orkestapay.orkestapay.client.model.clicktopay

import kotlinx.serialization.Serializable

@Serializable
data class ClickToPayMethod(val firstName: String, val lastName: String, val email: String, val phoneNumber: String)
