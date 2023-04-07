package com.example.csci4176_groupproject.models

import com.example.csci4176_groupproject.interfaces.Buyable

class Cosmetic(
    override val title: String,
    var description: String,
    var img: Int,
    override val cost: Int,
    var locked: Boolean = true
) :
    Buyable