package com.example.foodnote.data.repository.diary_item_detail_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.firebase.FirebaseDataSource
import com.example.foodnote.data.datasource.diary_item_detail_repository.DiaryItemDetailDatasource
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import kotlinx.coroutines.flow.Flow

class DiaryItemDetailRepositoryImpl(
    private val remoteDataSource: DiaryItemDetailDatasource,
    private val firebaseCalorieDataSource: FirebaseDataSource
) :
    DiaryItemDetailRepository {
    override suspend fun searchProduct(name: String): List<FoodDto> {
        return remoteDataSource.searchProduct(name)
    }

    override fun saveItem(item: DiaryItem, foodItem: FoodDto?): Flow<AppState<String>> =
        firebaseCalorieDataSource.saveDiaryItem(item, foodItem)

    override fun getSavedFoodCollection(
        idUser: String,
        diaryId: String
    ): Flow<AppState<List<FoodDto>>> =
        firebaseCalorieDataSource.getSavedFoodCollection(idUser, diaryId)

}