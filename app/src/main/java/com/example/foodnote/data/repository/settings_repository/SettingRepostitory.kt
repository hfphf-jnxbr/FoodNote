package com.example.foodnote.data.repository.settings_repository

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun saveProfile(data: Profile, userId: String): Flow<AppState<String>>
    fun getProfile(userId: String): Flow<AppState<Profile?>>
}