package com.example.csci4176_groupproject

data class levelData(
    var time: Int = -1,
    var tried: Boolean = false,
    val id: Int,
    var locked: Boolean,
    var starsEarned: Int = 0
)