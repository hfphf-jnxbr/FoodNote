package com.example.foodnote.data.repository.datastore_pref_repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userId: Flow<String>
    suspend fun setUserId(userId: String)
}