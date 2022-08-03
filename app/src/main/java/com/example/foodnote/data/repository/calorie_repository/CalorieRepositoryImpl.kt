package com.example.foodnote.data.repository.calorie_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.firebase.FirebaseDataSource
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

class CalorieRepositoryImpl(private val firebaseCalorieDataSource: FirebaseDataSource) :
    CalorieRepository {
    override fun saveDiary(item: DiaryItem): Flow<AppState<String>> {
        return firebaseCalorieDataSource.saveDiaryItem(item, null)
    }

    override fun getDiaryCollection(
        date: String,
        idUser: String
    ): Flow<AppState<MutableList<DiaryItem>>> {
        return firebaseCalorieDataSource.getDiaryCollection(idUser, date)
    }

    override fun saveProfile(data: Profile, userId: String): Flow<AppState<String>> {
        TODO("Not yet implemented")
    }

    override fun getProfile(userId: String): Flow<AppState<Profile?>> {
        return firebaseCalorieDataSource.getProfileData(userId)
    }

}