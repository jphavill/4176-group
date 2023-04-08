package com.example.csci4176_groupproject.models

import android.widget.ImageView

abstract class Tile(val tileImageView: ImageView) {
    private var xPos = 0
    private var yPos = 0

    // Getters
    fun getXPos(): Int {
        return xPos
    }
    fun getYPos(): Int {
        return yPos
    }

    // Setters
    fun setXPos(newXPos: Int){
        xPos = newXPos
    }
    fun setYPos(newYPos: Int){
        yPos = newYPos
    }
}