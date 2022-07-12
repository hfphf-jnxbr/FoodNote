package com.example.foodnote.data.repository.calorie_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.datasource.calorire_datasource.firebase.FirebaseCalorieDataSource
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow

class CalorieRepositoryImpl(private val firebaseCalorieDataSource: FirebaseCalorieDataSource) :
    CalorieRepository {
    override fun saveDiary(item: DiaryItem): Flow<String> {
        return firebaseCalorieDataSource.saveDiaryItem(item, null)
    }

    override fun getDiaryCollection(
        date: String,
        idUser: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return firebaseCalorieDataSource.getDiaryCollection(idUser, date)
    }

}