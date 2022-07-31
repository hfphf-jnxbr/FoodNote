package com.example.foodnote.data.interactor.calorie_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.BaseIntеractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface CalorieCalculatorInteractor : BaseIntеractor {
    suspend fun calculateTotalData(list: List<DiaryItem>, profile: Profile): TotalFoodResult
    fun saveDiary(item: DiaryItem): Flow<AppState<String>>
    fun getDiaryCollection(date: String, idUser: String): Flow<AppState<MutableList<DiaryItem>>>
}