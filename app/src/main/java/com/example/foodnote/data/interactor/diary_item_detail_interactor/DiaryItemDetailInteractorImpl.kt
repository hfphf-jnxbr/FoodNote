package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.repository.diary_item_detail_repository.DiaryItemDetailRepository

class DiaryItemDetailInteractorImpl(private val repos: DiaryItemDetailRepository) :
    DiaryItemDetailInteractor {
    override fun searchFood(name: String): List<FoodDto> {
        TODO("Not yet implemented")
    }
}