package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.model.food.FoodDto

interface DiaryItemDetailRepository {
    suspend fun searchProduct(name: String): List<FoodDto>
}