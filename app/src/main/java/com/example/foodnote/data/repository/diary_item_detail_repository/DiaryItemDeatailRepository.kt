package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.model.food.FoodDto

interface DiaryItemDetailRepository {
    fun searchProduct(): List<FoodDto>
}