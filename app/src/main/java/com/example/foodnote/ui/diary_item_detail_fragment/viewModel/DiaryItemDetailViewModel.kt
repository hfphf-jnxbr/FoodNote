package com.example.foodnote.ui.diary_item_detail_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DiaryItemDetailViewModel(
    private val dataStorePref: UserPreferencesRepository,
    private val interactor: DiaryItemDetailInteractor
) :
    BaseViewModel<SampleState>(dataStorePref) {
    init {
        stateLiveData.value = SampleState()
    }

    override fun onCleared() {
        super.onCleared()
        stateLiveData.value = SampleState()
    }

    fun saveDiaryItem(item: DiaryItem) {
        viewModelScope.launch {
            stateLiveData.value = stateLiveData.value?.copy(diaryItem = item)
        }
    }

    fun searchFood(name: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                interactor.searchFood(name)
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(foodDtoItems = it)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }

    fun saveFood(item: FoodDto) = interactor.saveDiaryItem(stateLiveData.value!!.diaryItem!!, item)


    fun calculateTotalData(list: List<FoodDto>) {
        viewModelScope.launch {
            kotlin.runCatching {
                interactor.calculateTotalData(list)
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(totalFoodResult = it)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }

    fun saveTotalCalculate(item: TotalFoodResult):
            Flow<AppState<String>> {
        val diaryItem = stateLiveData.value!!.diaryItem!!.apply {
            calories = item.calorieSum.toLong()
            proteinSum = item.proteinSum
            fatSum = item.fatSum
            carbSum = item.carbohydrateSum
        }
        return interactor.saveDiaryItem(diaryItem, null)
    }


    fun calculateTotalData() {

    }

    fun getSavedFoodCollection(idUser: String, dbId: String) =
        interactor.getSavedFoodCollection(idUser, dbId)


}