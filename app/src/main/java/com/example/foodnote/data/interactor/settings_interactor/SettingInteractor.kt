package com.example.foodnote.data.interactor.settings_interactor

import com.example.foodnote.data.base.BaseIntеractor

interface SettingInteractor : BaseIntеractor {
    suspend fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        age: String,
        male: Boolean,
        female: Boolean
    ): List<Pair<SettingColumnRequire, Boolean>>
}