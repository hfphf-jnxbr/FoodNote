package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.calorie_interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class CalorieCalculatorViewModel(
    private val interactor: CalorieCalculatorInteractor,
    dataStorePref: UserPreferencesRepository
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

    fun generateRandomItem(idUser: String, time: String, name: String): DiaryItem {
        val item = DiaryItem(
            name,
            0,
            time,
            SimpleDateFormat("dd.MMMM.YYYY").format(Date()),
            idUser,
            UUID.randomUUID().toString()
        )
        stateLiveData.value?.diaryList?.add(item)
        return item
    }

    suspend fun getDiary(idUser: String) = withContext(Dispatchers.IO) {
        interactor
            .getDiaryCollection(
                SimpleDateFormat("dd.MMMM.YYYY")
                    .format(Date()),
                idUser
            )
    }

    fun calculateTotalData() {
        viewModelScope.launch {
            kotlin.runCatching {
                stateLiveData.value?.diaryList?.let {
                    interactor.calculateTotalData(it)
                }
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(totalFoodResult = it)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }

    suspend fun saveDiary(item: DiaryItem) = withContext(Dispatchers.IO) {
        interactor.saveDiary(item)
    }

}