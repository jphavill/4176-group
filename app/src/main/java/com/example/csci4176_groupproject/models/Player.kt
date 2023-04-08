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
        val animationDuration: Long = (700 / crossedTiles.count()).toLong().coerceAtMost(200)
        for (groundTile in crossedTiles) {
            playerImageView.animate().apply {
                duration = animationDuration
                translationX(groundTile.xPos.toFloat())
                translationY(groundTile.yPos.toFloat())
            }.withEndAction {
                if (!groundTile.coloured)
                    groundTile.colourTile()
                playerPosX = groundTile.xPos
                playerPosY = groundTile.yPos
                playerGroundTile = groundTile
                playerIsMoving = false
            }
        }
    }
}