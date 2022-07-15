package com.example.foodnote.ui.noteBook.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewModelConstructorFragment : ViewModel(), ViewModelConstructorInterface {
    private val liveData = MutableLiveData<StateData>()

    override fun getLiveData() = liveData

    override fun sendServerToCal(foods: List<String>, weights: List<String>) {

        viewModelScope.launch {
            kotlin.runCatching {

                delay(200)

                "3600cals"
            }.onSuccess {

                // response.isSuccessful && response.body() != null
                if (true) {
                    liveData.value = StateData.Success(it)
                } else {
                    liveData.value = StateData.Error(Throwable("Not Found"))
                }
            }.onFailure {
                liveData.value = StateData.Error(Throwable("NetWork ERROR"))
            }
        }
    }

}