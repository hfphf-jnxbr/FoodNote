package com.example.foodnote.data.datasource.diary_item_detail_repository

import com.example.foodnote.data.datasource.api.ApiService
import com.example.foodnote.data.model.food.Food

class DiaryItemDetailDatasourceImpl(private val apiService: ApiService) :
    DiaryItemDetailDatasource {
    override fun searchProduct(name: String): Food {
        TODO("Not yet implemented")
    }
}