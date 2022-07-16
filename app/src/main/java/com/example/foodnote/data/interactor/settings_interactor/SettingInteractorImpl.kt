package com.example.foodnote.data.interactor.settings_interactor

import com.example.foodnote.data.repository.SettingRepository.SettingRepository

class SettingInteractorImpl(repo: SettingRepository) : SettingInteractor {
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
}