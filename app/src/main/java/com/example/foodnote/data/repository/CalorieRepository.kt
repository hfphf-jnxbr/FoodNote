package com.example.foodnote.data.repository

import com.example.foodnote.data.model.DiaryItem

interface CalorieRepository {
    fun saveDiary(item: DiaryItem)
    fun getDiaryCollection(date: String, idUser: String): MutableList<DiaryItem>
}