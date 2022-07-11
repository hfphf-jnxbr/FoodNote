package com.example.foodnote.ui.diary_item_detail_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.diary_item_detail_interactor.DiaryItemDetailInteractor
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryItemDetailViewModel(
    private val dataStorePref: UserPreferencesRepository,
    private val interactor: DiaryItemDetailInteractor
) :
    BaseViewModel<SampleState>(dataStorePref) {
    fun searchFood() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
            }.onSuccess {
                //stateLiveData.value = stateLiveData.value?.copy(calorie = it)
            }.onFailure {
                //stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }
}