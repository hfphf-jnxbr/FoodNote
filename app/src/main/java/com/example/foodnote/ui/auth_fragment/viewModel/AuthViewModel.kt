package com.example.foodnote.ui.auth_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<SampleState>() {
    fun saveUserId(id: String) {
        viewModelScope.launch {
            dataStorePref.setUserId(id)
        }
    }

    fun getUserId() = dataStorePref.userId
}