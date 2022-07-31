package com.example.foodnote.data.interactor.calorie_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.repository.calorie_repository.CalorieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CalorieCalculatorInteractorImpl(private val repository: CalorieRepository) :
    CalorieCalculatorInteractor {
    override fun saveDiary(item: DiaryItem): Flow<AppState<String>> {
        return repository.saveDiary(item)
    }

    override fun getDiaryCollection(
        date: String,
        idUser: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return repository.getDiaryCollection(date, idUser)
    }

    override suspend fun calculateTotalData(list: List<DiaryItem>): TotalFoodResult {
        return withContext(Dispatchers.Default) {
            val calorieSum = list.sumOf {
                it.calories ?: 0
            }
            val proteinSum = list.sumOf {
                it.proteinSum ?: 0.0
            }

            val fatSum = list.sumOf {
                it.fatSum ?: 0.0
            }

            val carbSum = list.sumOf {
                it.carbSum ?: 0.0
            }
            TotalFoodResult(0.0, calorieSum.toDouble(), 0.0, proteinSum, 0.0, fatSum, 0.0, carbSum)
        }
    }
}