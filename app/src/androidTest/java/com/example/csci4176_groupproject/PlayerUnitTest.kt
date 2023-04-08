package com.example.csci4176_groupproject

import android.content.Context
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class PlayerUnitTest {
    private var playerStartingPos = arrayOf(250, 350)
    private lateinit var instrumentationContext: Context
    private lateinit var playerImageView: ImageView
    private lateinit var startingGroundTileImageView: ImageView
    private lateinit var startingGroundTile: GroundTile
    private lateinit var player: Player

    @Before
    fun testSetup(){
        instrumentationContext = ApplicationProvider.getApplicationContext()
        playerImageView = ImageView(instrumentationContext)
        startingGroundTileImageView = ImageView(instrumentationContext)
        startingGroundTile = GroundTile(startingGroundTileImageView)
        startingGroundTile.tileImageView.translationX = 250f
        startingGroundTile.tileImageView.translationY = 350f
        startingGroundTile.setXPos(playerStartingPos[0])
        startingGroundTile.setYPos(playerStartingPos[1])
        player = Player(playerImageView, playerStartingPos[0], playerStartingPos[1], startingGroundTile)
    }

    /**
     * Checks players current position compared to desired initial location after setup assignment.
     */
    @Test
    fun playerStartingLocation_IsCorrect() {
        assertEquals(playerStartingPos[0], player.getPlayerPosX())
        assertEquals(playerStartingPos[1], player.getPlayerPosY())
    }

    /**
     * Performs player movement test to ensure the player's new location is correctly set after the
     * movement.
     */
    @Test
    fun movePlayerHorizontal_NewLocation_IsCorrect() {
        val crossedTiles = ArrayList<GroundTile>()
        for(i in 1..5){
            val newGroundTile = GroundTile(ImageView(instrumentationContext))
            newGroundTile.tileImageView.translationX = 250f - (50 * i)
            newGroundTile.tileImageView.translationY = 350f
            newGroundTile.setXPos((250 - (50 * i)))
            newGroundTile.setYPos(350)
            crossedTiles.add(newGroundTile)
        }
        player.movePlayerPos(crossedTiles)
//        Log.d("PlayerPos", "[${player.getPlayerPosX()}, ${player.getPlayerPosY()}]")
//        Log.d("LastTilePos", "[${crossedTiles.last().getXPos()}, ${crossedTiles.last().getYPos()}]")
        assertEquals(0, player.getPlayerPosX())
        assertEquals(350, player.getPlayerPosY())
    }

    /**
     * Performs player movement test to ensure the player has the proper final GroundTile linked
     * to the player objects 'playerGroundTile' instance variable.
     */
    @Test
    fun playersGroundTile_IsCorrect() {
        assertEquals(startingGroundTile, player.getPlayerGroundTile())
        val crossedTiles = ArrayList<GroundTile>()
        for(i in 1..5){
            val newGroundTile = GroundTile(ImageView(instrumentationContext))
            newGroundTile.tileImageView.translationX = 250f - (50 * i)
            newGroundTile.tileImageView.translationY = 350f
            newGroundTile.setXPos((250 - (50 * i)))
            newGroundTile.setYPos(350)
            crossedTiles.add(newGroundTile)
        }
        player.movePlayerPos(crossedTiles)
        assertEquals(crossedTiles.last(), player.getPlayerGroundTile())
    }
}