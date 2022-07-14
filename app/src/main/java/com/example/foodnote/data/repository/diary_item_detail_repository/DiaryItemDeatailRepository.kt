package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import kotlinx.coroutines.flow.Flow

interface DiaryItemDetailRepository {
    suspend fun searchProduct(name: String): List<FoodDto>
    fun saveItem(item: DiaryItem, foodItem: FoodDto?): Flow<AppState<String>>
    fun getSavedFoodCollection(idUser: String, diaryId: String): Flow<AppState<List<FoodDto>>>
}