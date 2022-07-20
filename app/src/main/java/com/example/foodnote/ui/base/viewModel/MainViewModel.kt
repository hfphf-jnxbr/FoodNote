package com.example.foodnote.ui.base.viewModel

import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository

class MainViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<String>(dataStorePref) {
    fun setDataIntoSubscribe() {
        stateLiveData.value = "SUBSCRIBE"
    }
}