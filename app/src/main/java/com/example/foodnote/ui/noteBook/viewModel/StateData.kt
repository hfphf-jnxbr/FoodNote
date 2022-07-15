package com.example.foodnote.ui.noteBook.viewModel

sealed class StateData {
    data class Success(val data: String?) : StateData()
    data class Loading(val loading: String) : StateData()
    data class Error(val error: Throwable) : StateData()
}