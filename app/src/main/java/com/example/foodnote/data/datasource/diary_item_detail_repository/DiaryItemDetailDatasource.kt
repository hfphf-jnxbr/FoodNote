package com.example.foodnote.data.datasource.diary_item_detail_repository

import com.example.foodnote.data.model.food.Food

interface DiaryItemDetailDatasource {
    suspend fun searchProduct(name: String): Food
}