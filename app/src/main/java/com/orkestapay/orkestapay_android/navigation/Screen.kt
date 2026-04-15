package com.orkestapay.orkestapay_android.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail")
}