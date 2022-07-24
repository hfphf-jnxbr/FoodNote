package com.example.foodnote.ui.base.viewModel

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository

class MainViewModel(private val dataStorePref: UserPreferencesRepository) :
    BaseViewModel<AppState<*>>(dataStorePref) {
    fun initCircle(totalFoodResult: TotalFoodResult) {
        stateLiveData.value = AppState.Success(totalFoodResult)
    }
}