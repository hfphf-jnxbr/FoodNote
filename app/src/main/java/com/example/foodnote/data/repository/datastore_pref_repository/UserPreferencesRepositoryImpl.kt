package com.example.foodnote.data.repository.datastore_pref_repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepositoryImpl.PreferencesKeys.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    UserPreferencesRepository {
    private val TAG: String = "UserPreferencesRepo"

    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("sort_order")
    }

    override val userId: Flow<String> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // No type safety.
            preferences[USER_ID] ?: ""
        }

    override suspend fun setUserId(userId: String) {
        dataStore.edit { settings ->
            settings[USER_ID] = userId
        }
    }
}