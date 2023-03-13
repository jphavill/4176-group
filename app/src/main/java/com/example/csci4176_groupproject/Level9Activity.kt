package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.csci4176_groupproject.databinding.ActivityLevel9Binding
import kotlin.math.abs


class Level9Activity : AppCompatActivity() {
    private var player: Player? = null;
    private var colouredTileCount = 0
    private val wallTiles: ArrayList<Tile> = ArrayList()
    private val groundTiles: ArrayList<Tile> = ArrayList()
    private val tileMap: ArrayList<Pair<Int, ArrayList<Tile>>> = ArrayList()

    private lateinit var detector: GestureDetectorCompat

    private lateinit var binding: ActivityLevel9Binding
    private lateinit var fullscreenContent: FrameLayout

    private var isFullscreen: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevel9Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.Level9FullscreenContent


        detector = GestureDetectorCompat(this, GestureListener())

        val backToHomeButton = findViewById<ImageButton>(R.id.BackToHomeButton)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }

        val settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)


        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.SettingsDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.settings_dialog, null)
            builder.setView(view)

            // set the state of the settings
            val colorBlindModeView = view.findViewById<Switch>(R.id.colorBlindSwitch)
            colorBlindModeView.isChecked = settingPrefs.getBoolean("colorBlind", false)
            val hapticsSwitchView = view.findViewById<Switch>(R.id.hapticsSwitch)
            hapticsSwitchView.isChecked = settingPrefs.getBoolean("haptics", true)
            val soundView = view.findViewById<ToggleButton>(R.id.soundToggle)
            soundView.isChecked = settingPrefs.getBoolean("sound", false)

            val applyButton = view.findViewById<Button>(R.id.applyButton)
            applyButton.setOnClickListener {

                // save state of settings
                val editor: SharedPreferences.Editor = settingPrefs.edit()
                editor.putBoolean("colorBlind", colorBlindModeView.isChecked)
                editor.putBoolean("haptics", hapticsSwitchView.isChecked)
                editor.putBoolean("sound", soundView.isChecked)
                editor.apply()

                builder.dismiss()
            }
            val cancelButton = view.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
//              if the cancel button is hit, don't save settings and exit
                builder.cancel()
            }
//          the user must hit either the cancel or apply button to close the dialog
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }

        // Setup wall tiles
        val wallTilesImageViews =
            getViewsByTag(findViewById(R.id.Level1FullscreenContent), "wallTile")

        if (wallTilesImageViews != null) {
            for (wallTileImageView in wallTilesImageViews) {
                val wallTile = WallTile(wallTileImageView as ImageView)
                wallTiles.add(wallTile)

                wallTileImageView.post {
                    val location = IntArray(2)
                    wallTileImageView.getLocationInWindow(location)
                    wallTile.setXPos(location[0])
                    wallTile.setYPos(location[1])
                    var tileAdded = false;
                    if(tileMap.isNotEmpty()){
                        val tileRowList = tileMap.filter { p -> p.first == wallTile.getYPos() }
                        if(tileRowList.isNotEmpty()){
                            val tileRow = tileRowList[0]
                            if(tileRow.second.isNotEmpty()) {
                                for(tile in tileRow.second){
                                    if(wallTile.getXPos() < tile.getXPos()) {
                                        tileAdded = true;
                                        tileRow.second.add(tileRow.second.indexOf(tile), wallTile)
                                        break;
                                    }
                                }
                                if(!tileAdded){
                                    tileRow.second.add(wallTile)
                                }
                            }
                        }
                        else{
                            tileMap.add(Pair(wallTile.getYPos(), ArrayList()))
                            val tileRow = tileMap.filter { p -> p.first == wallTile.getYPos() }[0]
                            tileRow.second.add(wallTile)
                        }
                    }
                    else{
                        tileMap.add(Pair(wallTile.getYPos(), ArrayList()))
                        tileMap[0].second.add(wallTile)
                    }
                }
            }
        }

        // Setup ground tiles
        val groundTilesImageViews =
            getViewsByTag(findViewById(R.id.Level1FullscreenContent), "groundTile")

        if (groundTilesImageViews != null) {
            for (groundTileImageView in groundTilesImageViews) {
                val groundTile = GroundTile(groundTileImageView as ImageView)
                groundTiles.add(groundTile)

                groundTileImageView.post {
                    val tileLocation = IntArray(2)
                    groundTileImageView.getLocationInWindow(tileLocation)
                    groundTile.setXPos(tileLocation[0])
                    groundTile.setYPos(tileLocation[1])

                    var tileAdded = false;
                    if(tileMap.isNotEmpty()){
                        val tileRowList = tileMap.filter { p -> p.first == groundTile.getYPos() }
                        if(tileRowList.isNotEmpty()){
                            val tileRow = tileRowList[0]
                            if(tileRow.second.isNotEmpty()) {
                                for(tile in tileRow.second){
                                    if(groundTile.getXPos() < tile.getXPos()) {
                                        tileAdded = true;
                                        tileRow.second.add(tileRow.second.indexOf(tile), groundTile)
                                        break;
                                    }
                                }
                                if(!tileAdded){
                                    tileRow.second.add(groundTile)
                                }
                            }
                        }
                        else{
                            tileMap.add(Pair(groundTile.getYPos(), ArrayList()))
                            val tileRow = tileMap.filter { p -> p.first == groundTile.getYPos() }[0]
                            tileRow.second.add(groundTile)
                        }
                    }
                    else{
                        tileMap.add(Pair(groundTile.getYPos(), ArrayList()))
                        tileMap[0].second.add(groundTile)
                    }

                    val playerImageView = findViewById<ImageView>(R.id.playerImageView)
                    val playerLocation = IntArray(2)
                    groundTile.tileImageView.getLocationInWindow(playerLocation)
                    if(groundTile.tileImageView.tag.toString() == "groundTileStart"){
                        player = Player(playerImageView, playerLocation[0], playerLocation[1], groundTile)
                        player?.getPlayerImageView()?.translationX = playerLocation[0].toFloat()
                        player?.getPlayerImageView()?.translationY = playerLocation[1].toFloat()
                        colourTile(groundTile)
                    }
                }
            }
        }

        hideAndroidUI()
    }

    private fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View>? {
        val views: ArrayList<View> = ArrayList<View>()
        val childCount = root.childCount

        for (i in 0 until childCount) {
            val child: View = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag)!!)
            }
            if (child.tag != null && child.tag.toString().contains(tag)) {
                views.add(child)
            }
        }
        return views
    }

    private fun hideAndroidUI() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if(detector.onTouchEvent(event)){
            true
        } else{
            super.onTouchEvent(event)
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {
        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100

        override fun onFling(
            downEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // Swipe deltas
            val deltaX = moveEvent.x.minus(downEvent.x)
            val deltaY = moveEvent.y.minus(downEvent.y)

            // Check for horizontal or vertical swipe.
            return if(abs(deltaX) > abs(deltaY)) {
                if(abs(deltaX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if(deltaX < 0) {
                        this@Level9Activity.onSwipeLeft()
                    }
                    else {
                        this@Level9Activity.onSwipeRight()
                    }
                    return true
                }
                else{
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
            else {
                if(abs(deltaY) > swipeThreshold && abs(velocityY) > swipeVelocityThreshold) {
                    if(deltaY < 0) {
                        this@Level9Activity.onSwipeUp()
                    }
                    else {
                        this@Level9Activity.onSwipeDown()
                    }
                    return true
                }
                else{
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }
    fun onSwipeRight() {
        Toast.makeText(this, "Swiped Right", Toast.LENGTH_SHORT).show()
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player?.getPlayerPosY() }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player?.getPlayerGroundTile())
        if(columnIndex < tileRow.second.count()-2) {
            while (tileMap[tileRowIndex].second[columnIndex + 1] is GroundTile) {
                columnIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                val tile = tileMap[tileRowIndex].second[columnIndex]
                val tileLocation = IntArray(2)
                tile.tileImageView.getLocationInWindow(tileLocation)
                player?.movePlayerPos(tileLocation[0], tileLocation[1], crossedTiles.last())
                for(groundTile in crossedTiles){
                    groundTile.colourTile()
                }
            }
        }
    }

    fun onSwipeLeft() {
        Toast.makeText(this, "Swiped Left", Toast.LENGTH_SHORT).show()
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player?.getPlayerPosY() }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player?.getPlayerGroundTile())
        if(columnIndex > 1) {
            while (tileMap[tileRowIndex].second[columnIndex - 1] is GroundTile) {
                columnIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                val tile = tileMap[tileRowIndex].second[columnIndex]
                val tileLocation = IntArray(2)
                tile.tileImageView.getLocationInWindow(tileLocation)
                player?.movePlayerPos(tileLocation[0], tileLocation[1], crossedTiles.last())
                for(groundTile in crossedTiles){
                    groundTile.colourTile()
                }
            }
        }
    }

    fun onSwipeDown() {
        Toast.makeText(this, "Swiped Down", Toast.LENGTH_SHORT).show()
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player?.getPlayerPosY() }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        if(tileRowIndex < tileMap.count()-2) {
            val columnIndex = tileRow.second.indexOf(player?.getPlayerGroundTile())
            while (tileMap[tileRowIndex + 1].second[columnIndex] is GroundTile) {
                tileRowIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                val tile = tileMap[tileRowIndex].second[columnIndex]
                val tileLocation = IntArray(2)
                tile.tileImageView.getLocationInWindow(tileLocation)
                player?.movePlayerPos(tileLocation[0], tileLocation[1], crossedTiles.last())
                for(groundTile in crossedTiles){
                    groundTile.colourTile()
                }
            }
        }
    }

    fun onSwipeUp() {
        Toast.makeText(this, "Swiped Up", Toast.LENGTH_SHORT).show()
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player?.getPlayerPosY() }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        if(tileRowIndex > 1) {
            val columnIndex = tileRow.second.indexOf(player?.getPlayerGroundTile())
            while (tileMap[tileRowIndex - 1].second[columnIndex] is GroundTile) {
                tileRowIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                val tile = tileMap[tileRowIndex].second[columnIndex]
                val tileLocation = IntArray(2)
                tile.tileImageView.getLocationInWindow(tileLocation)
                player?.movePlayerPos(tileLocation[0], tileLocation[1], crossedTiles.last())
                for(groundTile in crossedTiles){
                    colourTile(groundTile)
                }
            }
        }
    }

    private fun colourTile(groundTile: GroundTile){
        groundTile.colourTile()
        colouredTileCount += 1;
        if(colouredTileCount == groundTiles.count())
            levelComplete()
    }

    private fun levelComplete(){

    }
}