package com.example.foodnote.ui.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodnote.data.repository.datastore_pref_repository.UserPreferencesRepository

abstract class BaseViewModel<T>(
    private val dataStorePref: UserPreferencesRepository,
    protected val stateLiveData: MutableLiveData<T> = MutableLiveData(),
    protected val composeLiveData: MutableLiveData<Boolean> = MutableLiveData()
) : ViewModel() {
    fun getComposeLiveData(): LiveData<Boolean> = composeLiveData

    fun showProgressBar() {
        composeLiveData.value = true
    }

    fun hideProgressBar() {
        composeLiveData.value = false
    }

    fun getStateLiveData(): LiveData<T> = stateLiveData

    fun getUserId() = dataStorePref.userId

}