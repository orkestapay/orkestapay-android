package tech.tcpip.orkestapay_android.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.tcpip.orkestapay_android.di.NetworkModule
import tech.tcpip.orkestapay_android.ui.screens.MainScreen
import tech.tcpip.orkestapay_android.ui.screens.detail.PaymentViewModel
import tech.tcpip.orkestapay_android.ui.screens.detail.DetailScreen

@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }

        composable("${Screen.Detail.route}/{amount}/{email}/{paymentMethod}/{deviceSession}") { backStackEntry ->
            val amount = backStackEntry.arguments?.getString("amount") ?: "0"
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val paymentMethod = backStackEntry.arguments?.getString("paymentMethod") ?: ""
            val deviceSession = backStackEntry.arguments?.getString("deviceSession") ?: ""

            Log.d("amount", amount)

            val viewModel = viewModel<PaymentViewModel>(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return PaymentViewModel(NetworkModule.repository) as T
                    }
                }
            )
            DetailScreen(viewModel, amount, email, paymentMethod, deviceSession)
        }
    }
}