package com.example.foodnote.ui.recipes_fragment

import android.util.Log
import androidx.lifecycle.*
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.datasource.recipes_datasource.RepositoryRecipesImpl
import com.example.foodnote.data.model.recipes.RecipesList
import com.example.foodnote.data.model.recipes.RecipesX
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class RecipesViewModel(private val repositoryRecipesImpl: RepositoryRecipesImpl) : ViewModel(){


    private val _listRecipes = MutableLiveData<RecipesList>()
    val listRecipes: LiveData<RecipesList>
        get() = _listRecipes


    fun searchRecipesByIngr(ingr:String) {

        viewModelScope.launch {
            val response = repositoryRecipesImpl.searchRecipes(ingr)
            Log.i("youTag","${response.hints}")
            _listRecipes.postValue(response)

        }

    }

    fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
        observeForever(object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }
}