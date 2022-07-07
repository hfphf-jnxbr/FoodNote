package com.example.foodnote.data.interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.CalorieRepository
import kotlinx.coroutines.flow.Flow

class CalorieCalculatorInteractorImpl(private val repository: CalorieRepository) :
    CalorieCalculatorInteractor {
    override fun saveDiary(item: DiaryItem): DiaryItem {
        return repository.saveDiary(item)
    }

    override fun getDiaryCollection(
        date: String,
        idUser: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return repository.getDiaryCollection(date, idUser)

    }

}