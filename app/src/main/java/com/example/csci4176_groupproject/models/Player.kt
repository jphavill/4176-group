package com.example.csci4176_groupproject.models

import android.widget.ImageView

class Player(private val playerImageView: ImageView, private var playerPosX: Int, private var playerPosY: Int, private var playerGroundTile: Tile) {
    var playerIsMoving: Boolean = false

    fun getPlayerGroundTile(): Tile {
        return playerGroundTile
    }
    fun getPlayerImageView(): ImageView {
        return playerImageView
    }

    fun getPlayerPosY(): Int {
        return playerPosY
    }

    private fun setPlayerPosX(newPlayerPosX: Int) {
        playerPosX = newPlayerPosX
    }
    private fun setPlayerPosY(newPlayerPosY: Int) {
        playerPosY = newPlayerPosY
    }
    private fun setPlayerGroundTile(newPositionGroundTile: GroundTile) {
        playerGroundTile = newPositionGroundTile
    }

    fun movePlayerPos(crossedTiles: ArrayList<GroundTile>){
        playerIsMoving = true
        val animationDuration: Long = (700 / crossedTiles.count()).toLong().coerceAtMost(200)
        for (groundTile in crossedTiles){
            playerImageView.animate().apply {
                duration = animationDuration
                translationX(groundTile.getXPos().toFloat())
                translationY(groundTile.getYPos().toFloat())
            }.withEndAction {
                if(!groundTile.getColoured())
                    groundTile.colourTile()
                setPlayerPosX(groundTile.getXPos())
                setPlayerPosY(groundTile.getYPos())
                setPlayerGroundTile(groundTile)
                playerIsMoving = false
            }
        }
    }
}