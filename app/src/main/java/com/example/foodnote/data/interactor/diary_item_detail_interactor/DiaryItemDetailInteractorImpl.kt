package com.example.foodnote.data.interactor.diary_item_detail_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
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

    override fun saveDiaryItem(item: DiaryItem, foodItem: FoodDto): Flow<AppState<String>> {
        return repo.saveItem(item, foodItem)
    }

    override fun getSavedFoodCollection(
        idUser: String,
        diaryId: String
    ): Flow<AppState<List<FoodDto>>> = repo.getSavedFoodCollection(idUser, diaryId)

    override suspend fun calculateTotalData(list: List<FoodDto>): TotalFoodResult {
        return withContext(Dispatchers.Default) {
            val calorieSum = list.sumOf {
                it.kiloCalories * list.size
            }
            val proteinSum = list.sumOf {
                it.protein * it.count
            }

            val fatSum = list.sumOf {
                it.fat * it.count
            }

            val carbSum = list.sumOf {
                it.carbohydrate * it.count
            }
            TotalFoodResult(calorieSum, proteinSum, fatSum, carbSum)
        }
    }

}