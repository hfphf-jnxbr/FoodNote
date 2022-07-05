package com.example.foodnote.data.interactor

import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.CalorieRepository

class CalorieCalculatorInteractorImpl(private val repository: CalorieRepository) :
    CalorieCalculatorInteractor {
    override fun saveDiary(item: DiaryItem) {
        repository.saveDiary(item)
    }

    override fun getDiaryCollection(date: String, idUser: String): MutableList<DiaryItem> {
        return repository.getDiaryCollection(date, idUser)
    }

}