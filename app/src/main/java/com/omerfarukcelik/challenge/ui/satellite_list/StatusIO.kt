package com.omerfarukcelik.challenge.ui.satellite_list

sealed class StatusIO<out T> {
    object Loading : StatusIO<Nothing>()
    data class Success<T>(val data: T) : StatusIO<T>()
    data class Error(val exception: Throwable) : StatusIO<Nothing>()
    object Empty : StatusIO<Nothing>()
}
