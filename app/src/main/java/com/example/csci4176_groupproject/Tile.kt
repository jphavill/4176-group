package com.example.csci4176_groupproject

import android.graphics.Color
import android.widget.ImageView

class Tile(val tileImageView: ImageView, val isGroundTile: Boolean) {
    private var coloured = false

    fun colourTile(){
        if(!coloured){
            coloured = true
            tileImageView.setColorFilter(Color.argb(80, 225, 68, 19))
        }
        else{
            coloured = false
            tileImageView.colorFilter = null
        }
    }
}