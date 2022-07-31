package com.example.foodnote.data.base

import com.example.foodnote.data.model.profile.Profile
import kotlinx.coroutines.flow.Flow

interface BaseRepository {
    fun saveProfile(data: Profile, userId: String): Flow<AppState<String>>
    fun getProfile(userId: String): Flow<AppState<Profile?>>
}