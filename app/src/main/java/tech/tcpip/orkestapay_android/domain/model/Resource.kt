package tech.tcpip.orkestapay_android.domain.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val errorData: ErrorResponse?) : Resource<Nothing>()
    data class Loading(val isLoading: Boolean) : Resource<Nothing>()
}