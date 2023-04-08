//Contributors: Jason Havill
package com.example.csci4176_groupproject.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestartLevelViewModel : ViewModel() {
    // obeservable to track when a level has been restarted
    private val mutableRestertLevel = MutableLiveData<Boolean>()
    val restartLevel: LiveData<Boolean> get() = mutableRestertLevel

    fun setRestartLevel(state: Boolean) {
        mutableRestertLevel.value = state
    }
}