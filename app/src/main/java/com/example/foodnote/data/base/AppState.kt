package com.example.foodnote.data.base

sealed interface AppState<T> {
    data class Success<T>(val data: T) : AppState<T>
    data class Error(val error: Throwable?) : AppState<Nothing>
    object Loading : AppState<Nothing>
    object Empty : AppState<Nothing>
}