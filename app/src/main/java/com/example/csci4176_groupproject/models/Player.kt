//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.models

import android.widget.ImageView

class Player(
    val playerImageView: ImageView,
    var playerPosX: Int,
    var playerPosY: Int,
    var playerGroundTile: Tile
) {
    // following Kotlin conventions getters and setters are not provided as they are auto generated
    // https://kotlinlang.org/docs/properties.html#backing-fields

    var playerIsMoving: Boolean = false

    fun movePlayerPos(crossedTiles: ArrayList<GroundTile>) {
        playerIsMoving = true
        // set the players position to the last tile it will move to
        playerPosX = crossedTiles.last().xPos
        playerPosY = crossedTiles.last().yPos
        playerGroundTile = crossedTiles.last()
        // animate the palyer moving through all of the tiles it will cross, and color as it moves
        val animationDuration: Long = (700 / crossedTiles.count()).toLong().coerceAtMost(200)
        for (groundTile in crossedTiles) {
            playerImageView.animate().apply {
                duration = animationDuration
                translationX(groundTile.xPos.toFloat())
                translationY(groundTile.yPos.toFloat())
            }.withEndAction {
                if (!groundTile.coloured)
                    groundTile.colourTile()
                playerIsMoving = false
            }
        }
    }
}