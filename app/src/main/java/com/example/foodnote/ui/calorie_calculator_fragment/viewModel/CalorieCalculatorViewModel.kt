package com.example.foodnote.ui.calorie_calculator_fragment.viewModel

import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch
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
}