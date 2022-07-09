package com.example.foodnote.ui.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import kotlinx.coroutines.launch

abstract class BaseViewModel<T>(
    private val dataStorePref: UserPreferencesRepository,
    protected val stateLiveData: MutableLiveData<T> = MutableLiveData()
) : ViewModel() {

    fun getStateLiveData(): LiveData<T> = stateLiveData
    override fun onCleared() {
        super.onCleared()
    }

    fun saveUserId(id: String) {
        viewModelScope.launch {
            dataStorePref.setUserId(id)
        }
    }

    fun getUserId() = dataStorePref.userId
}