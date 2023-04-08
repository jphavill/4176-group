package com.example.csci4176_groupproject

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class GroundTileUnitTest {
    private var groundTileLocation = arrayOf(250, 350)
    private lateinit var instrumentationContext: Context
    private lateinit var groundTileImageView: ImageView
    private lateinit var groundTile: GroundTile
    @Before
    fun testSetup(){
        instrumentationContext = ApplicationProvider.getApplicationContext()
        groundTileImageView = ImageView(instrumentationContext)
        groundTile = GroundTile(groundTileImageView)
        groundTile.setXPos(groundTileLocation[0])
        groundTile.setYPos(groundTileLocation[1])
        groundTile.setColorBlind(false)
    }

    /**
     * Checks groundTile location to ensure proper initial location after setup assignment.
     */
    @Test
    fun groundTileLocation_IsCorrect() {
        assertEquals(groundTileLocation[0], groundTile.getXPos())
        assertEquals(groundTileLocation[1], groundTile.getYPos())
    }

    /**
     * Colours the ground tile and ensures all aspects of the colouring process was successful.
     */
    @Test
    fun groundTileColoured_IsCorrect() {
        val testColouredFilter = PorterDuffColorFilter(Color.argb(80, 225, 68, 19), PorterDuff.Mode.SRC_ATOP)
        groundTile.colourTile()
        assertTrue(groundTile.getColoured())
        assertTrue(groundTile.tileImageView.colorFilter == testColouredFilter)
    }

    /**
     * Reverses the colouring process to ensure the colour resetting functionality functions
     * properly ensures all aspects of the uncolouring process was successful.
     */
    @Test
    fun groundTileUncoloured_IsCorrect() {
        val testColouredFilter = PorterDuffColorFilter(Color.argb(80, 225, 68, 19), PorterDuff.Mode.SRC_ATOP)
        val testUncolouredFilter = PorterDuffColorFilter(Color.argb(100, 161, 161, 161), PorterDuff.Mode.SRC_ATOP)
        groundTile.colourTile()
        assertTrue(groundTile.getColoured())
        assertTrue(groundTile.tileImageView.colorFilter == testColouredFilter)
        groundTile.uncolourTile()
        assertFalse(groundTile.getColoured())
        assertTrue(groundTile.tileImageView.colorFilter == testUncolouredFilter)
    }

    /**
     * Colours the ground tile and ensures all aspects of the colouring process was successful,
     * and ensures that the drawable image source is updated for the GroundTile object when in colour
     * blind mode.
     */
    @Test
    fun groundTileColoured_ColourBlindMode_IsCorrect() {
        val testColouredFilter = PorterDuffColorFilter(Color.argb(80, 225, 68, 19), PorterDuff.Mode.SRC_ATOP)
        groundTile.colourTile()
        assertTrue(groundTile.getColoured())
        assertTrue(groundTile.tileImageView.colorFilter == testColouredFilter)
        val originalDrawable = groundTile.tileImageView.drawable
        groundTile.setColorBlind(true)
        assertTrue(groundTile.tileImageView.drawable != originalDrawable)
    }

    /**
     * Colours the ground tile and ensures that the drawable image source is NOT updated for the GroundTile
     * object when NOT in colour blind mode.
     */
    @Test
    fun groundTileColoured_Drawable_IsCorrect() {

        val originalDrawable = groundTile.tileImageView.drawable
        groundTile.setColorBlind(false)
        groundTile.colourTile()
        assertTrue(groundTile.tileImageView.drawable == originalDrawable)
    }

    /**
     * Colours the ground tile and ensures that the drawable image source is updated if the GroundTile
     * was in colour blind mode and now no longer is in blind mode.
     */
    @Test
    fun groundTileColoured_ColourBlindMode_Off_IsCorrect() {
        groundTile.colourTile()
        groundTile.setColorBlind(true)
        val originalDrawable = groundTile.tileImageView.drawable
        groundTile.setColorBlind(false)
        assertTrue(groundTile.tileImageView.drawable != originalDrawable)
    }
}