package com.example.foodnote.data.repository

import com.example.foodnote.data.datasource.calorire_datasource.firebase.FirebaseCalorieDataSource
import com.example.foodnote.data.model.DiaryItem

class CalorieRepositoryImpl(private val firebaseCalorieDataSource: FirebaseCalorieDataSource) :
    CalorieRepository {
    override fun saveDiary(item: DiaryItem) {
        firebaseCalorieDataSource.saveDiaryItem(item)
    }

    override fun getDiaryCollection(date: String, idUser: String): MutableList<DiaryItem> {
        return firebaseCalorieDataSource.getDiaryCollection(idUser, date)
    }

}