package com.example.foodnote.data.datasource.calorire_datasource.firebase

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import kotlinx.coroutines.flow.Flow


interface FirebaseCalorieDataSource {
    fun saveDiaryItem(diaryItem: DiaryItem, foodItem: FoodDto?): Flow<String>
    fun getDiaryCollection(idUser: String, date: String): Flow<AppState<MutableList<DiaryItem>>>

}