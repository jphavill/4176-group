//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject

import android.content.Context
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import com.example.csci4176_groupproject.models.GroundTile
import com.example.csci4176_groupproject.models.Player
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Ignore

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
        startingGroundTile.xPos = playerStartingPos[0]
        startingGroundTile.yPos = playerStartingPos[1]
        player = Player(playerImageView, playerStartingPos[0], playerStartingPos[1], startingGroundTile)
    }

    /**
     * Checks players current position compared to desired initial location after setup assignment.
     */
    @Test
    fun playerStartingLocation_IsCorrect() {
        assertEquals(playerStartingPos[0], player.playerPosX)
        assertEquals(playerStartingPos[1], player.playerPosY)
    }

    /**
     * Performs player movement test to ensure the player's new location is correctly set after the
     * horizontal movement.
     */
    @Test
    fun movePlayerHorizontal_NewLocation_IsCorrect() {
        val crossedTiles = ArrayList<GroundTile>()
        for(i in 1..5){
            val newGroundTile = GroundTile(ImageView(instrumentationContext))
            newGroundTile.tileImageView.translationX = 250f - (50 * i)
            newGroundTile.tileImageView.translationY = 350f
            newGroundTile.xPos = (250 - (50 * i))
            newGroundTile.yPos = 350
            crossedTiles.add(newGroundTile)
        }
        player.movePlayerPos(crossedTiles)
        assertEquals(0, player.playerPosX)
        assertEquals(350, player.playerPosY)
    }

    /**
     * Performs player movement test to ensure the player's new location is correctly set after the
     * vertical movement.
     */
    @Test
    fun movePlayerVertical_NewLocation_IsCorrect() {
        val crossedTiles = ArrayList<GroundTile>()
        for(i in 1..5){
            val newGroundTile = GroundTile(ImageView(instrumentationContext))
            newGroundTile.tileImageView.translationX = 250f
            newGroundTile.tileImageView.translationY = 350f - (50 * i)
            newGroundTile.xPos = 250
            newGroundTile.yPos = (350 - (50 * i))
            crossedTiles.add(newGroundTile)
        }
        player.movePlayerPos(crossedTiles)
        assertEquals(250, player.playerPosX)
        assertEquals(100, player.playerPosY)
    }

    /**
     * Performs player movement test to ensure the player has the proper final GroundTile linked
     * to the player objects 'playerGroundTile' instance variable.
     */
    @Test
    fun playersGroundTile_IsCorrect() {
        assertEquals(startingGroundTile, player.playerGroundTile)
        val crossedTiles = ArrayList<GroundTile>()
        for(i in 1..5){
            val newGroundTile = GroundTile(ImageView(instrumentationContext))
            newGroundTile.tileImageView.translationX = 250f - (50 * i)
            newGroundTile.tileImageView.translationY = 350f
            newGroundTile.xPos = (250 - (50 * i))
            newGroundTile.yPos = 350
            crossedTiles.add(newGroundTile)
        }
        player.movePlayerPos(crossedTiles)
        assertEquals(crossedTiles.last(), player.playerGroundTile)
    }
}