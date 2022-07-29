package com.example.foodnote.ui.diary_item_detail_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class DiaryItemDetailViewModel(
    dataStorePref: UserPreferencesRepository,
    private val interactor: DiaryItemDetailInteractor
) :
    BaseViewModel<AppState<*>>(dataStorePref) {
    private var diaryItem: DiaryItem? = null
    fun saveDiaryItem(item: DiaryItem) {
        diaryItem = item
    }

    fun searchFood(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                interactor.searchFood(name)
            }.onSuccess {
                stateLiveData.value = AppState.Success(it)
            }.onFailure {
                stateLiveData.value = AppState.Error<Throwable>(it)
            }
        }
    }

    fun saveFood(item: FoodDto) {
        viewModelScope.launch {
            interactor.saveDiaryItem(diaryItem!!, item).collect {
                when (it) {
                    is AppState.Error -> stateLiveData.value = it
                    is AppState.Loading -> stateLiveData.value = it
                    is AppState.Success -> stateLiveData.value = it
                }
            }
        }
    }


    fun calculateTotalData(list: List<FoodDto>) {
        viewModelScope.launch {
            kotlin.runCatching {
                interactor.calculateTotalData(list)
            }.onSuccess {
                stateLiveData.value = AppState.Success(it)
            }.onFailure {
                stateLiveData.value = AppState.Error<Throwable>(it)
            }
        }
    }

    fun saveTotalCalculate(item: TotalFoodResult) {
        viewModelScope.launch {
            kotlin.runCatching {
                val diaryItem = diaryItem!!.apply {
                    calories = item.calorieSum
                    proteinSum = item.proteinSum
                    fatSum = item.fatSum
                    carbSum = item.carbohydrateSum
                }
                interactor.saveDiaryItem(diaryItem, null).collect {
                    when (it) {
                        is AppState.Error -> {
                            stateLiveData.value = it
                        }
                        is AppState.Loading -> {
                            stateLiveData.value = it
                        }
                        is AppState.Success -> {
                            stateLiveData.value = it
                        }
                    }
                }
            }

        }

    }

    fun getSavedFoodCollection(idUser: String, dbId: String) {
        viewModelScope.launch {
            interactor.getSavedFoodCollection(idUser, dbId).collect {
                when (it) {
                    is AppState.Error -> {
                        stateLiveData.value = it
                    }
                    is AppState.Loading -> {
                        stateLiveData.value = it
                    }
                    is AppState.Success -> {
                        stateLiveData.value = it
                        calculateTotalData(it.data)
                    }
                }
            }
        }
    }

}