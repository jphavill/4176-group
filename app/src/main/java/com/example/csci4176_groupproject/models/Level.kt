package com.example.csci4176_groupproject.models

import com.example.csci4176_groupproject.interfaces.Buyable

data class Level(
    var time: Int = -1,
    var tried: Boolean = false,
    val id: Int,
    var locked: Boolean,
    var starsEarned: Int = 0,
    override val cost: Int = 1,
    override val title: String = "Level $id"
): Buyable