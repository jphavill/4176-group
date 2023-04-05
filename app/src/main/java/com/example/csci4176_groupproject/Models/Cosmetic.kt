package com.example.csci4176_groupproject.Models

import com.example.csci4176_groupproject.Buyable

class Cosmetic(override val title: String, var description: String, var img: Int, override val cost:Int, var locked:Boolean = true):
    Buyable