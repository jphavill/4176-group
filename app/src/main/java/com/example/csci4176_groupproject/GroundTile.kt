package com.example.csci4176_groupproject

import android.graphics.Color
import android.widget.ImageView

class GroundTile(tileImageView: ImageView) : Tile(tileImageView){
    private var coloured = false

    fun getColoured(): Boolean {
        return coloured
    }

    fun colourTile(){
        if(!coloured){
            coloured = true
            tileImageView.setColorFilter(Color.argb(80, 225, 68, 19))
        }
    }
}