package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.repository.diary_item_detail_repository.DiaryItemDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiaryItemDetailInteractorImpl(private val repo: DiaryItemDetailRepository) :
    DiaryItemDetailInteractor {
    override suspend fun searchFood(name: String): List<FoodDto> {
        return withContext(Dispatchers.IO) {
            repo.searchProduct(name)
        }
    }
}