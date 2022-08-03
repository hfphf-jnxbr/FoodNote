package com.example.foodnote.data.interactor.settings_interactor

import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.profile.Profile
import com.example.foodnote.data.repository.settings_repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SettingInteractorImpl(private val repo: SettingRepository) : SettingInteractor {
    override suspend fun checkRequireColumn(
        type: String,
        weight: String,
        height: String,
        age: String,
        male: Boolean,
        female: Boolean
    ): List<Pair<SettingColumnRequire, Boolean>> {
        return listOf(
            Pair(SettingColumnRequire.MODE, type.isNotEmpty()),
            Pair(SettingColumnRequire.WEIGHT, weight.isNotEmpty()),
            Pair(SettingColumnRequire.HEIGHT, height.isNotEmpty()),
            Pair(SettingColumnRequire.AGE, age.isNotEmpty()),
            Pair(SettingColumnRequire.SEX, male || female)
        )

    }

    override suspend fun saveProfile(data: Profile, userId: String): Flow<AppState<String>> {
        calculateProfile(data)
        return repo.saveProfile(data, userId)
    }

    private suspend fun calculateProfile(data: Profile) {
        withContext(Dispatchers.Default) {
            data.calories = calculateCalorie(
                data.male ?: false,
                data.female ?: false,
                data.weight ?: 0,
                data.height ?: 0,
                data.age ?: 0
            )

            data.protein = calculateProtein(data.calories ?: 0)
            data.fat = calculateFat(data.calories ?: 0)
            data.carb = calculateCarb(data.calories ?: 0)
        }
    }

    private fun calculateCalorie(
        male: Boolean,
        female: Boolean,
        weight: Int,
        height: Int,
        age: Int
    ): Int {
        val result =
            (WEIGHT_COEFF * weight) + (HEIGHT_COEFF * height) - (AGE_COEFF * age) + (if (male) -161 else 5)
        return result.toInt()
    }

    private fun calculateProtein(calorie: Int): Int {
        return (calorie * (PROTEIN_PERCENT / 100.0) / PROTEIN_GRAM).toInt()
    }

    private fun calculateFat(calorie: Int): Int {
        return (calorie * (FAT_PERCENT / 100.0) / FAT_GRAM).toInt()
    }

    private fun calculateCarb(calorie: Int): Int {
        return (calorie * (CARB_PERCENT / 100.0) / CARB_GRAM).toInt()
    }

    override fun getProfile(userId: String): Flow<AppState<Profile?>> {
        return repo.getProfile(userId)
    }

    private companion object {
        const val WEIGHT_COEFF = 10
        const val HEIGHT_COEFF = 6.25
        const val AGE_COEFF = 5

        const val PROTEIN_GRAM = 4
        const val FAT_GRAM = 9
        const val CARB_GRAM = 4

        const val PROTEIN_PERCENT = 30
        const val FAT_PERCENT = 30
        const val CARB_PERCENT = 40
    }
}