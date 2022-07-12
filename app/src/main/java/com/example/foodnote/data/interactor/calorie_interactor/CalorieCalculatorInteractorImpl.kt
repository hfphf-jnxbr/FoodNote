package com.example.foodnote.data.interactor.calorie_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.repository.calorie_repository.CalorieRepository
import kotlinx.coroutines.flow.Flow

class CalorieCalculatorInteractorImpl(private val repository: CalorieRepository) :
    CalorieCalculatorInteractor {
    override fun saveDiary(item: DiaryItem): Flow<String> {
        return repository.saveDiary(item)
    }

    override fun getDiaryCollection(
        date: String,
        idUser: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return repository.getDiaryCollection(date, idUser)

    }

}