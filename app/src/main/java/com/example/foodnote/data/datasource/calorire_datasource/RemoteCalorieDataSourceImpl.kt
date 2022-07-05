package com.example.foodnote.data.datasource.calorire_datasource

import com.example.foodnote.data.datasource.calorire_datasource.firebase.CalorieDataSource
import com.example.foodnote.data.model.DiaryItem

class RemoteCalorieDataSourceImpl : CalorieDataSource {
    override fun saveDiaryItem(diaryItem: DiaryItem) {
        TODO("Not yet implemented")
    }

    override fun getDiaryCollection(idUser: String, date: String): MutableList<DiaryItem> {
        TODO("Not yet implemented")
    }

}