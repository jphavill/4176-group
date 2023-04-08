//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.models

import android.graphics.Color
import android.widget.ImageView
import com.example.csci4176_groupproject.R

class GroundTile(tileImageView: ImageView) : Tile(tileImageView) {
    // a tile in the level that a player can move over
    // following Kotlin conventions getters and setters are not provided as they are auto generated
    // https://kotlinlang.org/docs/properties.html#backing-fields
    var coloured = false

    fun colourTile() {
        if (!coloured) {
            coloured = true
            tileImageView.setColorFilter(Color.argb(80, 225, 68, 19))
        }
    }

    fun uncolourTile() {
        coloured = false
        tileImageView.setImageResource(R.drawable.ground_tile_foreground)
        tileImageView.setColorFilter(Color.argb(100, 161, 161, 161))
    }

    fun setColorBlind(colorBlindMode: Boolean) {
        // in colour blind mode instead of changing the tint of the background a new image
        // of an X is swapped in to indicate being coloured
        if (colorBlindMode && coloured) {
            tileImageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        } else {
            tileImageView.setImageResource(R.drawable.ground_tile_foreground)
        }
    }
}