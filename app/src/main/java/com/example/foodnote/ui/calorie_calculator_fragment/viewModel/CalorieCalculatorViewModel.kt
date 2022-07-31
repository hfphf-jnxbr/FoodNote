package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.calorie_interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class CalorieCalculatorViewModel(
    private val interactor: CalorieCalculatorInteractor,
    dataStorePref: UserPreferencesRepository
) :
    BaseViewModel<AppState<*>>(dataStorePref) {
    private val diaryList = ArrayList<DiaryItem>()
    private val currentDate = SimpleDateFormat("dd.MMMM.YYYY").format(Date())
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
                stateLiveData.value = AppState.Success(it)
            }.onFailure {
                stateLiveData.value = AppState.Error<Throwable>(it)
            }
        }
    }

    fun generateRandomItem(idUser: String, time: String, name: String): DiaryItem {
        val item = DiaryItem(
            name,
            0,
            time,
            currentDate,
            idUser,
            UUID.randomUUID().toString()
        )
        diaryList.add(item)
        return item
    }

    fun getDiary(idUser: String) {
        viewModelScope.launch {
            interactor
                .getDiaryCollection(
                    currentDate,
                    idUser
                ).collect {
                    stateLiveData.value = it
                }
        }
    }


    fun calculateTotalData() {
        viewModelScope.launch {
            kotlin.runCatching {
                diaryList.let {
                    interactor.calculateTotalData(it)
                }
            }.onSuccess {
                stateLiveData.value = AppState.Success(it)
            }.onFailure {
                stateLiveData.value = AppState.Error<Throwable>(it)
            }
        }
    }

    fun saveDiary(item: DiaryItem) {
        viewModelScope.launch {
            interactor.saveDiary(item).collect {
                stateLiveData.value = it
            }
        }
    }
}