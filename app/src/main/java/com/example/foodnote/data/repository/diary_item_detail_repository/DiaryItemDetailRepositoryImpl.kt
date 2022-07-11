package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasource
import com.example.foodnote.data.model.food.FoodDto

class DiaryItemDetailRepositoryImpl(private val dataSource: DiaryItemDetailDatasource) :
    DiaryItemDetailRepository {
    override fun searchProduct(): List<FoodDto> {
        TODO("Not yet implemented")
    }
}