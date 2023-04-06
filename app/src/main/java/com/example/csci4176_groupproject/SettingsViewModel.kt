package com.example.csci4176_groupproject

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

    fun setPlayerSkin(skin: Int) {
        mutablePlayerSkin.value = skin
    }

    fun setHaptics(state: Boolean) {
        mutableHaptics.value = state
    }

    fun setColorBlindMode(state: Boolean) {
        mutableColorBlindMode.value = state
    }
}