package com.example.foodnote.ui.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodnote.data.base.SampleState

abstract class BaseViewModel<T : SampleState>(
    protected val stateLiveData: MutableLiveData<T> = MutableLiveData()
) : ViewModel() {

    fun getStateLiveData(): LiveData<T> = stateLiveData
    override fun onCleared() {
        super.onCleared()
    }
}