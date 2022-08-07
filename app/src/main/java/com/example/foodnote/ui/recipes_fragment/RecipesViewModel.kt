package com.example.foodnote.ui.recipes_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.databaseRoom.dao.RecipesDao
import com.example.foodnote.data.datasource.recipes_datasource.RepositoryRecipesImpl
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.data.model.recipes.RecipesList
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import com.example.foodnote.utils.totoEntityRecipes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipesViewModel(
    dataStorePref: UserPreferencesRepository,
    private val repositoryRecipesImpl: RepositoryRecipesImpl,
    private val dataBase:RecipesDao
) : BaseViewModel<AppState<*>>(dataStorePref) {


    private val _listRecipes = MutableLiveData<RecipesList>()
    val listRecipes: LiveData<RecipesList>
        get() = _listRecipes

    val successAddRecipes = MutableLiveData<Boolean>()


    fun searchRecipesByIngr(ingr: String) {
        viewModelScope.launch {
            val response = repositoryRecipesImpl.searchRecipes(ingr)
            _listRecipes.postValue(response)
        }
    }

    fun addRecipesInDatabase(recipes: Recipes){
        viewModelScope.launch(Dispatchers.IO) {
            if(dataBase.getAllRecipesFavorite().filter { item ->
                    (item == recipes.totoEntityRecipes())
                }.isEmpty()) {
                dataBase.addRecipesFavorite(recipes.totoEntityRecipes())
                successAddRecipes.postValue( true)
            }
        }
    }


}