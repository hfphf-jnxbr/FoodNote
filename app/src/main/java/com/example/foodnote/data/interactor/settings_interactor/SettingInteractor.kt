package com.example.foodnote.data.interactor.settings_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface SettingInteractor {
    suspend fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        male: Boolean,
        female: Boolean
    ): List<Pair<SettingColumnRequire, Boolean>>

    fun saveProfile(data: Profile, userId: String): Flow<AppState<String>>
    fun getProfile(userId: String): Flow<AppState<Profile?>>
}