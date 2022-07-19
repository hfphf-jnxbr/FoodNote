package com.example.foodnote.ui.recipes_fragment

import androidx.lifecycle.ViewModel
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository
import com.example.foodnote.ui.base.viewModel.BaseViewModel

class RecipesViewModel( private val dataStorePref: UserPreferencesRepository) : BaseViewModel<SampleState>(dataStorePref){
}