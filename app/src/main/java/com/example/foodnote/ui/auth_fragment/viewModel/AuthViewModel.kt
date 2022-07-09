package com.example.foodnote.ui.auth_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<SampleState>(dataStorePref) {
    init {
        stateLiveData.value = SampleState()
    }

    fun saveUserId(id: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                dataStorePref.setUserId(id)
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(isSuccess = true)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }
}