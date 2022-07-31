package com.example.foodnote.data.interactor.calorie_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.model.profile.Profile
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

    override suspend fun saveProfile(data: Profile, userId: String): Flow<AppState<String>> {
        TODO("Not yet implemented")
    }

    override fun getProfile(userId: String): Flow<AppState<Profile?>> {
        return repository.getProfile(userId)
    }

    override suspend fun calculateTotalData(
        list: List<DiaryItem>,
        profile: Profile
    ): TotalFoodResult {
        return withContext(Dispatchers.Default) {

            val calorieSum = list.sumOf {
                it.calories ?: 0
            }
            val proteinSum = list.sumOf {
                it.proteinSum ?: 0
            }

            val fatSum = list.sumOf {
                it.fatSum ?: 0
            }

            val carbSum = list.sumOf {
                it.carbSum ?: 0
            }

            TotalFoodResult(
                profile.calories ?: 0,
                calorieSum,
                profile.protein ?: 0,
                proteinSum,
                profile.fat ?: 0,
                fatSum,
                profile.carb ?: 0,
                carbSum
            )
        }
    }
}