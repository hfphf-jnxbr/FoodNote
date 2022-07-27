package com.example.foodnote.ui.settings_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.settings_interactor.SettingColumnRequire
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractor
import com.example.foodnote.data.model.profile.Profile
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
        age: String,
        male: Boolean,
        female: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                interactor.checkRequireColumn(type, weight, height, age, male, female)
            }.onSuccess { data ->
                val errorData = data.filter { item -> !item.second }
                withContext(Dispatchers.Main) {
                    if (errorData.isEmpty()) {
                        stateLiveData.value =
                            AppState.Success(Pair(SettingColumnRequire.SUCCESS_DATA, true))
                    } else {
                        errorData.forEach { state ->
                            stateLiveData.value = AppState.Success(state)
                        }
                    }
                }
            }.onFailure { error ->
                stateLiveData.postValue(AppState.Error<Throwable>(error))
            }
        }
    }

    fun saveProfileData(
        type: String,
        weight: String,
        height: String,
        age: String,
        male: Boolean,
        female: Boolean,
        userId: String
    ) {
        viewModelScope.launch {
            val profile = Profile(
                weight = weight.toInt(),
                height = height.toInt(),
                age = age.toInt(),
                meta = type,
                male = male,
                female = female
            )
            interactor.saveProfile(profile, userId).collect {
                    stateLiveData.value = it
            }
        }
    }

    fun getProfileData(idUser: String) {
        viewModelScope.launch {
            interactor.getProfile(idUser).collect {
                withContext(Dispatchers.Main) {
                    stateLiveData.value = it
                }
            }
        }
    }


}