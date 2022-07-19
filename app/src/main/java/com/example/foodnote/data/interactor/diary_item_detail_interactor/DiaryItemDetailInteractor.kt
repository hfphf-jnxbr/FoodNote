package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
import kotlinx.coroutines.flow.Flow

interface DiaryItemDetailInteractor {
    suspend fun searchFood(name: String): List<FoodDto>
    fun saveDiaryItem(item: DiaryItem, foodItem: FoodDto?): Flow<AppState<String>>
    fun getSavedFoodCollection(idUser: String, diaryId: String): Flow<AppState<List<FoodDto>>>
    suspend fun calculateTotalData(list: List<FoodDto>): TotalFoodResult
}