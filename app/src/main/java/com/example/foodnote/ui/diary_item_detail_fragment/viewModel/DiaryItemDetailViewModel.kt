package com.example.foodnote.ui.diary_item_detail_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractor
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
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
}