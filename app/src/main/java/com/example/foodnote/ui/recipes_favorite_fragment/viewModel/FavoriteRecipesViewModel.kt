package com.example.foodnote.ui.recipes_favorite_fragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.databaseRoom.dao.RecipesDao
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.data.interactor.settings_interactor.SettingColumnRequire
import com.example.foodnote.data.interactor.settings_interactor.SettingInteractor
import com.example.foodnote.data.model.profile.Profile
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteRecipesViewModel(
    dataStorePref: UserPreferencesRepository,
    val interactor: SettingInteractor,
    private val dataBase:RecipesDao
) : BaseViewModel<AppState<*>>(dataStorePref) {

    init {
        getFavoriteRecipes()
    }

    private fun getFavoriteRecipes(){
        stateLiveData.value = AppState.Loading<List<EntitiesRecipes>>()
        viewModelScope.launch(Dispatchers.IO) {

            stateLiveData.postValue(AppState.Success(dataBase.getAllRecipesFavorite()))
        }

    }

    fun removeRecipes(recipes: EntitiesRecipes) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBase.removeRecipesFavorite(recipes)
            stateLiveData.postValue(AppState.Success(dataBase.getAllRecipesFavorite()))
        }
    }
}