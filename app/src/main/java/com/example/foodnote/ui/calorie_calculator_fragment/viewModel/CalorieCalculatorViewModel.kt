package com.example.foodnote.ui.calorie_calculator_fragment.viewModel


import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.calorie_interactor.CalorieCalculatorInteractor
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.data.model.profile.Profile
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalorieCalculatorViewModel(
    private val interactor: CalorieCalculatorInteractor,
    dataStorePref: UserPreferencesRepository
) :
    BaseViewModel<AppState<*>>(dataStorePref) {
    private var diaryList = mutableListOf<DiaryItem>()
    private var profileData: Profile? = null
    private val currentDate = SimpleDateFormat("dd.MMMM.YYYY").format(Date())
    fun generateItem(idUser: String, time: String, name: String): DiaryItem {
        val item = DiaryItem(
            name,
            0,
            time,
            currentDate,
            idUser,
            UUID.randomUUID().toString()
        )
        diaryList.add(item)
        return item
    }

    fun getDiary(idUser: String) {
        viewModelScope.launch {
            val diaryItems = interactor
                .getDiaryCollection(
                    currentDate,
                    idUser
                )
            val userItem = interactor.getProfile(idUser)
            diaryItems.zip(userItem) { diaries, user ->
                stateLiveData.value = diaries
                val totalResult = when {
                    diaries is AppState.Success && user is AppState.Success -> {
                        diaryList = diaries.data
                        profileData = user.data
                        AppState.Success(calculateTotalData())
                    }
                    else -> {
                        AppState.Error<Throwable>(null)
                    }
                }
                totalResult
            }.collect {
                stateLiveData.value = it
            }
        }
    }


    private suspend fun calculateTotalData(): TotalFoodResult? {
        if (diaryList.isNotEmpty() && profileData != null) {
            return interactor.calculateTotalData(diaryList, profileData!!)
        }
        return null
    }

    fun saveDiary(item: DiaryItem) {
        viewModelScope.launch {
            interactor.saveDiary(item).collect {
                stateLiveData.value = AppState.Success(diaryList)
            }
        }
    }
}