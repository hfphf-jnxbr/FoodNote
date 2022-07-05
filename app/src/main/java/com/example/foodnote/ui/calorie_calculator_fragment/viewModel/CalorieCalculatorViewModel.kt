package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class CalorieCalculatorViewModel(private val interactor: CalorieCalculatorInteractor) :
    BaseViewModel<SampleState>() {
    init {
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

    fun initRandomList() {
        viewModelScope.launch {
            kotlin.runCatching {
                val list = ArrayList<DiaryItem>(10)
                for (i in 0..3) {
                    list.add(
                        DiaryItem(
                            "item $i",
                            Random.nextInt(100, 500),
                            SimpleDateFormat("hh:mm:ss").format(Date()),
                            SimpleDateFormat("dd.MMMM.YYYY").format(Date()),
                            "mail@mail.ru"
                        )
                    )
                }
                list
            }.onSuccess {
                stateLiveData.value = stateLiveData.value?.copy(diaryList = it)
            }.onFailure {
                stateLiveData.value = stateLiveData.value?.copy(error = it)
            }
        }
    }

    fun getDiary() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val date = SimpleDateFormat("dd.MMMM.YYYY").format(Date())
                interactor.getDiaryCollection(date, "mail@mail.ru")
            }.onSuccess {

            }.onFailure {
                stateLiveData.postValue(stateLiveData.value?.copy(error = it))
            }
        }
    }

    fun saveDiary(item: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                interactor.saveDiary(item)
            }.onSuccess {

            }.onFailure {
                stateLiveData.postValue(stateLiveData.value?.copy(error = it))
            }
        }
    }
}