//Contributors: Jason Havill
package com.example.csci4176_groupproject.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StarCountViewModel : ViewModel() {
    // observable to track changes to the users starCount (essentially bank account)
    private val mutableStarCount = MutableLiveData<Int>()
    val starCount: LiveData<Int> get() = mutableStarCount

    fun setCount(count: Int) {
        mutableStarCount.value = count
    }
}