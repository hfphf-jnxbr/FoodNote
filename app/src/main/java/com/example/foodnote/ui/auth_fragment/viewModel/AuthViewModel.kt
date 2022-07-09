package com.example.foodnote.ui.auth_fragment.viewModel

import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel

class AuthViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<SampleState>(dataStorePref) {

}