//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.models

import android.widget.ImageView

abstract class Tile(val tileImageView: ImageView) {
    // the level map is made up of these individual tiles
    // following Kotlin conventions getters and setters are not provided as they are auto generated
    // https://kotlinlang.org/docs/properties.html#backing-fields
    var xPos = 0
    var yPos = 0
}