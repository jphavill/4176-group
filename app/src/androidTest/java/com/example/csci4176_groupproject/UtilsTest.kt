package com.example.csci4176_groupproject

import android.content.Context
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.csci4176_groupproject.models.GroundTile
import com.example.csci4176_groupproject.models.Player
import com.example.csci4176_groupproject.utils.TimeCalcs
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class LevelUnitTest {
    private var playerStartingPos = arrayOf(250, 350)
    private lateinit var instrumentationContext: Context
    private lateinit var playerImageView: ImageView
    private lateinit var startingGroundTileImageView: ImageView
    private lateinit var startingGroundTile: GroundTile
    private lateinit var player: Player
    private lateinit var appContext: Context

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
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    /**
     * Checks that the milliseconds between two times is being calculated correctly
     */
    @Test
    fun timeCalculationTest(){
        val offsetSeconds: Long = 5
        val offsetMinutes: Long = 4
        val offsetHours: Long = 3
        val totalOffset = ((offsetSeconds * 1000) + (offsetMinutes * 60000)
                + (offsetHours * 3600000)).toInt()
        val start = LocalDateTime.now()
        val end = start.plusSeconds(offsetSeconds).plusMinutes(offsetMinutes).plusHours(offsetHours)
        val result = TimeCalcs().timeDifference(start, end)
        assertEquals(result,  totalOffset)
    }



}