package com.example.foodnote.data.datasource.calorire_datasource.firebase

import com.example.foodnote.data.model.DiaryItem

interface FirebaseCalorieDataSource {
    fun saveDiaryItem(diaryItem: DiaryItem, idUser: String, date: String)
    fun getDiaryCollection(idUser: String, date: String): MutableList<DiaryItem>

}