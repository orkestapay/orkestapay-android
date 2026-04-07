package com.orkestapay.orkestapay.core.clicktopay

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ClickToPayCallbackActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.data?.let { uri ->
            ClickToPayInternal.notifyResult(uri)
        }

        val intent = Intent(this, ClickToPayControlActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}