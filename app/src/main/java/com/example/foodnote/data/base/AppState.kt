package com.example.foodnote.data.base

sealed interface AppState<T> {
    data class Success<T>(val data: List<Any>) : AppState<T>
    data class Error<T>(val error: Throwable?) : AppState<T>
    class Loading<T> : AppState<T>
    object Empty : AppState<Nothing>
}