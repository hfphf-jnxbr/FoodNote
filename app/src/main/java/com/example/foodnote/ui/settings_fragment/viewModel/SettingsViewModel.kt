package com.example.foodnote.ui.settings_fragment.viewModel

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractor
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel

class SettingsViewModel(dataStorePref: UserPreferencesRepository, interactor: SettingInteractor) :
    BaseViewModel<AppState<*>>(dataStorePref)