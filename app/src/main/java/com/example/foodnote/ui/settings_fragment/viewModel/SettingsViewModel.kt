package com.example.foodnote.ui.settings_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractor
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    dataStorePref: UserPreferencesRepository,
    val interactor: SettingInteractor
) :
    BaseViewModel<AppState<*>>(dataStorePref) {
    fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        male: Boolean,
        female: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                interactor.checkRequireColumn(type, weight, height, male, female)
            }.onSuccess { data ->
                withContext(Dispatchers.Main) {
                    data.forEach { state ->
                        stateLiveData.value = AppState.Success(state)
                    }
                }
            }.onFailure { error ->
                stateLiveData.postValue(AppState.Error<Throwable>(error))
            }
        }
    }
}