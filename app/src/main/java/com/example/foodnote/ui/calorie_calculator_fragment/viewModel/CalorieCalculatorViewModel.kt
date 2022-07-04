package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class CalorieCalculatorViewModel() : BaseViewModel<AppState<*>>() {
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
                stateLiveData.value = AppState.Error(it)
            }
        }
    }

    fun initRandomList() {
        viewModelScope.launch {
            kotlin.runCatching {
                val list = ArrayList<DiaryItem>(10)
                for (i in 0..10) {
                    list.add(
                        DiaryItem(
                            "item $i",
                            Random.nextInt(100, 500),
                            SimpleDateFormat("hh:mm:ss").format(Date())
                        )
                    )
                }
                list
            }.onSuccess {
                stateLiveData.value = AppState.Success(it)
            }.onFailure {
                stateLiveData.value = AppState.Error(it)
            }
        }
    }
}