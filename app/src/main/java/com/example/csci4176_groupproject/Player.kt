package com.example.csci4176_groupproject

import android.os.Build
import android.widget.ImageView

class Player(private val playerImageView: ImageView, private var playerPosX: Int, private var playerPosY: Int, private var playerGroundTile: Tile) {
    public var playerIsMoving: Boolean = false
    // Getters
    fun getPlayerGroundTile(): Tile {
        return playerGroundTile
    }
    fun getPlayerImageView(): ImageView {
        return playerImageView
    }
    fun getPlayerPosX(): Int {
        return playerPosX
    }
    fun getPlayerPosY(): Int {
        return playerPosY
    }

    // Setters
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
        val animationDuration: Long = (1000 / crossedTiles.count()).toLong().coerceAtMost(200)
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
            }
        }
    }
}