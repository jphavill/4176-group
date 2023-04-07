package com.example.csci4176_groupproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    private val mutablePlayerSkin = MutableLiveData<Int>()
    val playerSkin: LiveData<Int> get() = mutablePlayerSkin
    private val mutableHaptics = MutableLiveData<Boolean>()
    val haptics: LiveData<Boolean> get() = mutableHaptics
    private val mutableColorBlindMode = MutableLiveData<Boolean>()
    val colorBlindMode: LiveData<Boolean> get() = mutableColorBlindMode
    private val mutableResetLevels = MutableLiveData<Boolean>()
    // developer settings
    val resetLevels: LiveData<Boolean> get() = mutableResetLevels
    private val mutableResetStore = MutableLiveData<Boolean>()
    val resetStore: LiveData<Boolean> get() = mutableResetStore


    fun setPlayerSkin(skin: Int) {
        mutablePlayerSkin.value = skin
    }

    fun setHaptics(state: Boolean) {
        mutableHaptics.value = state
    }

    fun setColorBlindMode(state: Boolean) {
        mutableColorBlindMode.value = state
    }

    fun setResetLevels(state: Boolean) {
        mutableResetLevels.value = state
    }

    fun setResetStore(state: Boolean) {
        mutableResetStore.value = state
    }
}