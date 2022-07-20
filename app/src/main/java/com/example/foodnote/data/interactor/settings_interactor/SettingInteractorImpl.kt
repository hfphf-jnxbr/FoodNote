package com.example.foodnote.data.interactor.settings_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.profile.Profile
import com.example.foodnote.data.repository.settings_repository.SettingRepository
import kotlinx.coroutines.flow.Flow

class SettingInteractorImpl(private val repo: SettingRepository) : SettingInteractor {
    override suspend fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        male: Boolean,
        female: Boolean
    ): List<Pair<SettingColumnRequire, Boolean>> {
        return listOf(
            Pair(SettingColumnRequire.MODE, type.isNotEmpty()),
            Pair(SettingColumnRequire.WEIGHT, weight.isNotEmpty()),
            Pair(SettingColumnRequire.HEIGHT, height.isNotEmpty()),
            Pair(SettingColumnRequire.SEX, male || female)
        )

    }

    override fun saveProfile(data: Profile, userId: String): Flow<AppState<String>> {
        return repo.saveProfile(data, userId)
    }

    override fun getProfile(userId: String): Flow<AppState<Profile>> {
        return repo.getProfile(userId)
    }
}