package com.example.csci4176_groupproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestartLevelViewModel : ViewModel() {
    private val mutableRestertLevel = MutableLiveData<Boolean>()
    val restartLevel: LiveData<Boolean> get() = mutableRestertLevel

    fun setRestartLevel(state: Boolean) {
        mutableRestertLevel.value = state
    }
}