package com.example.foodnote.data.repository.calorie_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.BaseRepository
import com.example.foodnote.data.model.DiaryItem
import kotlinx.coroutines.flow.Flow

interface CalorieRepository : BaseRepository {
    fun saveDiary(item: DiaryItem): Flow<AppState<String>>
    fun getDiaryCollection(date: String, idUser: String): Flow<AppState<MutableList<DiaryItem>>>
}