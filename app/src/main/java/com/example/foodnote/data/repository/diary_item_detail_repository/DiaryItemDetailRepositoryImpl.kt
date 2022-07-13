package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FirebaseCalorieDataSource
import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasource
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.utils.toFoodListDto
import kotlinx.coroutines.flow.Flow

class DiaryItemDetailRepositoryImpl(
    private val remoteDataSource: DiaryItemDetailDatasource,
    private val firebaseCalorieDataSource: FirebaseCalorieDataSource
) :
    DiaryItemDetailRepository {
    var list: List<FoodDto>? = null
    override suspend fun searchProduct(name: String): List<FoodDto> {
        val newList = remoteDataSource.searchProduct(name).toFoodListDto()
        list = newList
        return newList
    }

    override fun saveItem(item: DiaryItem, foodItem: FoodDto): Flow<AppState<String>> =
        firebaseCalorieDataSource.saveDiaryItem(item, foodItem)

    override fun getSavedFoodCollection(
        idUser: String,
        diaryId: String
    ): Flow<AppState<List<FoodDto>>> =
        firebaseCalorieDataSource.getSavedFoodCollection(idUser, diaryId)

}