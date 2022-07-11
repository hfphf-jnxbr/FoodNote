package com.example.foodnote.data.repository.calorie_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow

interface CalorieRepository {
    fun saveDiary(item: DiaryItem): DiaryItem
    fun getDiaryCollection(date: String, idUser: String): Flow<AppState<MutableList<DiaryItem>>>
}