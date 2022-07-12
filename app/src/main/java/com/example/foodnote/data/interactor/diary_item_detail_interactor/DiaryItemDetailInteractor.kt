package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import kotlinx.coroutines.flow.Flow

interface DiaryItemDetailInteractor {
    suspend fun searchFood(name: String): List<FoodDto>
    fun saveDiaryItem(item: DiaryItem, foodItem: FoodDto): Flow<String>
}