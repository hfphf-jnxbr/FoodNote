package com.example.foodnote.ui.splash_screen_fragment.viewModel

import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel

class SplashScreenViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<SampleState>(dataStorePref)