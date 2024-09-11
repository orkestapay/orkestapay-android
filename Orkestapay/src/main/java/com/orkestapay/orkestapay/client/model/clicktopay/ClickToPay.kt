package com.orkestapay.orkestapay.client.model.clicktopay

import java.io.Serializable

data class ClickToPay(val email: String? = null,
                      val firstName: String? = null,
                      val lastName: String? = null,
                      val phoneCountryCode: String? = null,
                      val phoneNumber: String? = null,
                      val isSandbox: Boolean? = null): Serializable
