package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasource
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.utils.toFoodListDto

class DiaryItemDetailRepositoryImpl(private val dataSource: DiaryItemDetailDatasource) :
    DiaryItemDetailRepository {
    override suspend fun searchProduct(name: String): List<FoodDto> {
        return dataSource.searchProduct(name).toFoodListDto()
    }
}