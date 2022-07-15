package com.example.foodnote.ui.noteBook.viewModel

import androidx.lifecycle.MutableLiveData

interface ViewModelConstructorInterface {
    fun getLiveData(): MutableLiveData<StateData>
    fun sendServerToCal(foods: List<String>, weights: List<String>)
}