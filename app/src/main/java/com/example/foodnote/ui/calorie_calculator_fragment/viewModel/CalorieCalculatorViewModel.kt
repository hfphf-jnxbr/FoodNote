package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class CalorieCalculatorViewModel(
    private val interactor: CalorieCalculatorInteractor,
    private val dataStorePref: UserPreferencesRepository
) :
    BaseViewModel<SampleState>(dataStorePref) {
    init {
        stateLiveData.value = SampleState()
    }

    override fun onCleared() {
        super.onCleared()
        stateLiveData.value = SampleState()
    }

    fun initCalorie() {
        viewModelScope.launch {
            kotlin.runCatching {
                Triple(
                    Pair(
                        Random.nextInt(0, 150),
                        Random.nextInt(150, 200)
                    ),
                    Pair(
                        Random.nextInt(0, 150),
                        Random.nextInt(150, 200)
                    ),
                    Pair(
                        Random.nextInt(0, 150),
                        Random.nextInt(150, 200)
                    ),
                )
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(calorie = it)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }

    fun generateRandomItem(idUser: String): DiaryItem {
        val item = DiaryItem(
            "item ${Random.nextInt(500, 7000)}",
            Random.nextLong(100, 500),
            SimpleDateFormat("hh:mm").format(Date()),
            SimpleDateFormat("dd.MMMM.YYYY").format(Date()),
            idUser,
            UUID.randomUUID().toString()
        )
        stateLiveData.value?.diaryList?.add(item)
        return item
    }

    fun getDiary(idUser: String) = interactor
        .getDiaryCollection(
            SimpleDateFormat("dd.MMMM.YYYY")
                .format(Date()),
            idUser
        )


    fun saveDiary(item: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                interactor.saveDiary(item)
            }.onSuccess {
                stateLiveData.postValue(stateLiveData.value?.copy(lastAddItem = it))
            }.onFailure {
                stateLiveData.postValue(stateLiveData.value?.copy(error = it))
            }
        }
    }
}