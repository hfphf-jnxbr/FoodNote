package com.example.foodnote.data.repository.settings_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.firebase.FirebaseDataSource
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

class SettingRepositoryImpl(private val fireBaseDataSource: FirebaseDataSource) :
    SettingRepository {
    override fun saveProfile(data: Profile, userId: String): Flow<AppState<String>> {
        return fireBaseDataSource.saveProfileData(profile = data, userId)
    }

    override fun getProfile(userId: String): Flow<AppState<Profile?>> {
        return fireBaseDataSource.getProfileData(userId)
    }
}