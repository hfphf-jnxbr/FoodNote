package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.repository.diary_item_detail_repository.DiaryItemDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DiaryItemDetailInteractorImpl(private val repo: DiaryItemDetailRepository) :
    DiaryItemDetailInteractor {
    override suspend fun searchFood(name: String): List<FoodDto> {
        return withContext(Dispatchers.IO) {
            repo.searchProduct(name)
        }
    }

    override fun saveDiaryItem(item: DiaryItem, foodItem: FoodDto): Flow<String> {
        return repo.saveItem(item, foodItem)
    }
}