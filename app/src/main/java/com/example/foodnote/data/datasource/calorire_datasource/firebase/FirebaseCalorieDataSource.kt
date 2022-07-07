package com.example.foodnote.data.datasource.calorire_datasource.firebase

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow


interface FirebaseCalorieDataSource {
    fun saveDiaryItem(diaryItem: DiaryItem): DiaryItem
    fun getDiaryCollection(idUser: String, date: String): Flow<AppState<MutableList<DiaryItem>>>

}