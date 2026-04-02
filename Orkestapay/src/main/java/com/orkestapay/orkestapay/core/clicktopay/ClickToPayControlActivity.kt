package com.orkestapay.orkestapay.core.clicktopay

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.graphics.toColorInt

class ClickToPayControlActivity: ComponentActivity() {
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        ClickToPayInternal.currentCallback?.onClosed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.getStringExtra("EXTRA_URL")
        if (url != null) {
            launcher.launch(
                getCustomTabsIntent(
                    uri = url.toUri()
                ).intent
            )
        } else {
            finish()
        }
    }

    fun getCustomTabsIntent(uri: Uri): CustomTabsIntent {

        val tabColor = CustomTabColorSchemeParams.Builder()
        tabColor.setNavigationBarColor("#e87600".toColorInt())

        val intentCustomTabs = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(tabColor.build())
            .setDownloadButtonEnabled(false)
            .setBookmarksButtonEnabled(false)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .setInstantAppsEnabled(false)
            .setBackgroundInteractionEnabled(false)
            .build()
        intentCustomTabs.intent.setData(uri)

        return intentCustomTabs
    }
}