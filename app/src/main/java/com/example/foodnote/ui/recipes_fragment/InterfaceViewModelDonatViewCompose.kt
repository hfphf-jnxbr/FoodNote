package com.example.foodnote.ui.recipes_fragment

import androidx.lifecycle.MutableLiveData
import com.example.foodnote.ui.noteBook.stateData.StateDataCompose
import com.example.foodnote.ui.noteBook.stateData.StateDataDonat

interface InterfaceViewModelDonatViewCompose {
    fun getLiveData() : MutableLiveData<StateDataDonat>
    fun setStateDonat(expanded : Boolean, angle : Boolean)
    fun setAnimated()
}