package com.example.foodnote.data.interactor.settings_interactor

interface SettingInteractor {
    suspend fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        male: Boolean,
        female: Boolean
    ): List<Pair<SettingColumnRequire, Boolean>>
}