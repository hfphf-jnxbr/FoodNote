package com.example.foodnote.data.base

sealed interface AppState<T> {
    data class Success<T>(val data: T) : AppState<T>
    data class Error<T>(val error: Throwable?) : AppState<T>
    class Loading<T> : AppState<T>
}