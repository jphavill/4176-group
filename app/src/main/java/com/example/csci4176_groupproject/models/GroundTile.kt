package com.example.csci4176_groupproject.models

import android.graphics.Color
import android.widget.ImageView
import com.example.csci4176_groupproject.R

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

    fun uncolourTile(){
        coloured = false
        tileImageView.setImageResource(R.drawable.ground_tile_foreground)
        tileImageView.setColorFilter(Color.argb(100, 161, 161, 161))
    }

    fun setColorBlind(colorBlindMode: Boolean){
        if(colorBlindMode && coloured){
            tileImageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        } else {
            tileImageView.setImageResource(R.drawable.ground_tile_foreground)
        }
    }
}