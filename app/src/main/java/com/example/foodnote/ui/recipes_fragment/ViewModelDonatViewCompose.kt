package com.example.foodnote.ui.recipes_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodnote.ui.noteBook.stateData.StateDataDonat

class ViewModelDonatViewCompose : ViewModel() , InterfaceViewModelDonatViewCompose {

    private val liveData = MutableLiveData<StateDataDonat>()
    override fun getLiveData() = liveData

    private var expanded = false
    private var angle = true

    override fun setStateDonat(expanded : Boolean, angle : Boolean) {
        this.expanded = expanded
        this.angle = angle
        liveData.value = StateDataDonat.SetData(expanded, angle)
    }

    override fun setAnimated() {
        this.expanded = !expanded
        this.angle = !angle
        liveData.value =  StateDataDonat.SetData(expanded, angle)
    }
}