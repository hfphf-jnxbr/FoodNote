package com.example.foodnote.data.base.firebase

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow


interface FirebaseDataSource {
    fun saveProfileData(profile: Profile, idUser: String): Flow<AppState<String>>
    fun getProfileData(idUser: String): Flow<AppState<Profile>>
    fun saveDiaryItem(diaryItem: DiaryItem, foodItem: FoodDto?): Flow<AppState<String>>
    fun getDiaryCollection(idUser: String, date: String): Flow<AppState<MutableList<DiaryItem>>>
    fun getSavedFoodCollection(idUser: String, diaryId: String): Flow<AppState<List<FoodDto>>>
}