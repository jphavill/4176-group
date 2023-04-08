package com.example.csci4176_groupproject.models

import com.example.csci4176_groupproject.interfaces.Buyable

data class Level(
    // time set to -1 to indicate a user hasn't completed (on completion time would be > 0
    var time: Int = -1,
    // by default a user hasn't tried a level yet
    var tried: Boolean = false,
    val id: Int,
    var locked: Boolean,
    // by default a user has earned 0 stars
    var starsEarned: Int = 0,
    // all levels cost 1 star
    // both cost and title are provided for the buyable interface to work with the buy dialog
    override val cost: Int = 1,
    override val title: String = "Level $id"
) : Buyable
