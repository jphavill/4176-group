package com.example.csci4176_groupproject.Models

import android.widget.ImageView

abstract class Tile(val tileImageView: ImageView) {
    private var xPos = 0
    private var yPos = 0
//    private var leftTile: Tile? = null
//    private var rightTile: Tile? = null
//    private var aboveTile: Tile? = null
//    private var belowTile: Tile? = null

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