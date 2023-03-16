package com.example.csci4176_groupproject

import android.media.Image
import android.util.Log
import android.widget.ImageView

class Player(private val playerImageView: ImageView, private var playerPosX: Int, private var playerPosY: Int, private var playerGroundTile: Tile) {
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
    fun movePlayerPos(newXPos: Int, newYPos: Int, newPositionGroundTile: GroundTile){
        playerImageView.translationX = newXPos.toFloat()
        playerImageView.translationY = newYPos.toFloat()
        setPlayerPosX(newXPos)
        setPlayerPosY(newYPos)
        setPlayerGroundTile(newPositionGroundTile)

        val location = IntArray(2)
        playerImageView.getLocationInWindow(location)
    }
}