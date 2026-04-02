package com.orkestapay.orkestapay.core.clicktopay

import android.app.Activity
import android.os.Bundle

class ClickToPayCallbackActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { uri ->
            ClickToPayInternal.notifyResult(uri)
        }

        finish()
    }
}