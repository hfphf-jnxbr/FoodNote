package com.example.foodnote.data.datasource.diary_item_detail_repository

import com.example.foodnote.data.datasource.api.ApiService
import com.example.foodnote.data.model.food.FoodDto

class DiaryItemDetailDatasourceImpl(private val apiService: ApiService) :
    DiaryItemDetailDatasource {
    override suspend fun searchProduct(name: String): List<FoodDto> {
        return apiService.getProductByNameV2(name)
    }
}