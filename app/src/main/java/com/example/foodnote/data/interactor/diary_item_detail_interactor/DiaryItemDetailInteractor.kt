package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.food.FoodDto

interface DiaryItemDetailInteractor {
    suspend fun searchFood(name: String): List<FoodDto>
}