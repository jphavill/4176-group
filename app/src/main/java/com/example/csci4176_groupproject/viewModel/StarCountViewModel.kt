package com.example.csci4176_groupproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StarCountViewModel: ViewModel() {
    private val mutableStarCount = MutableLiveData<Int>()
    val starCount: LiveData<Int> get() = mutableStarCount

    fun setCount(count: Int) {
        mutableStarCount.value = count
    }
}