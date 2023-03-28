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
     * and ensures proper drawable image source is used for the GroundTile object when in colour
     * blind mode.
     */
    @Test
    fun groundTileColoured_ColourBlindMode_IsCorrect() {
        val testColouredFilter = PorterDuffColorFilter(Color.argb(80, 225, 68, 19), PorterDuff.Mode.SRC_ATOP)
        groundTile.colourTile()
        assertTrue(groundTile.getColoured())
        assertTrue(groundTile.tileImageView.colorFilter == testColouredFilter)
        Log.d("groundTile.tileImageView.drawable", groundTile.tileImageView.drawable.toString())
        groundTile.setColorBlind(true)

        val colourBlindDrawable: Drawable? = ResourcesCompat.getDrawable(instrumentationContext.resources, android.R.drawable.ic_menu_close_clear_cancel, null)
        Log.d("colourBlindDrawable", colourBlindDrawable.toString())
        Log.d("groundTile.tileImageView.drawable", groundTile.tileImageView.drawable.toString())
        assertTrue(colourBlindDrawable != null)
        assertTrue(groundTile.tileImageView.drawable == colourBlindDrawable)
    }
}