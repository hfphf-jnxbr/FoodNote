package com.example.foodnote.ui.base.viewModel

import com.example.foodnote.data.base.AppState

class MainViewModel : BaseViewModel<AppState<*>>() {
    var selectedItemId: Int? = null
}