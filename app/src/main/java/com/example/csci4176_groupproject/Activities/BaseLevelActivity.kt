package com.example.csci4176_groupproject.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.GestureDetectorCompat
import com.example.csci4176_groupproject.Data.CosmeticList
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.Dialogs.SettingsDialog
import com.example.csci4176_groupproject.Dialogs.WinDialog
import com.example.csci4176_groupproject.Models.*
import com.example.csci4176_groupproject.SettingsViewModel
import java.time.LocalDateTime
import kotlin.math.abs

abstract class BaseLevelActivity: BaseActivity() {
    abstract val levelId: Int
    private lateinit var player: Player
    private var colouredTileCount = 0
    private val wallTiles: ArrayList<Tile> = ArrayList()
    private val groundTiles: ArrayList<Tile> = ArrayList()
    private val tileMap: ArrayList<Pair<Int, ArrayList<Tile>>> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var startTime: LocalDateTime
    private var levelStarted = false
    private lateinit var detector: GestureDetectorCompat
    lateinit var fullScreenView: ViewGroup

    private val settingsViewModel: SettingsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsViewModel.colorBlindMode.observe(this) { state ->
            updateColorBlindMode(state)
        }
        settingsViewModel.playerSkin.observe(this) { skin ->
            updatePlayerSkin(skin)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun levelSetup(){
        val currentPlayerSkin = settingPrefs.getInt("playerSkin", CosmeticList().itemList[0].img)
        updatePlayerSkin(currentPlayerSkin)
        detector = GestureDetectorCompat(this, GestureListener())

        super.addTopBar("Level $levelId", "LevelSelectActivity")

        val resetButton = findViewById<ImageButton>(R.id.resetLevelsButton)
        resetButton.setOnClickListener{
            resetLevel()
        }



        setupWalls()
        setupGround()


    }

    fun setupWalls(){
        // Setup wall tiles
        val wallTilesImageViews =
            getViewsByTag(fullScreenView, "wallTile")

        for (wallTileImageView in wallTilesImageViews) {
            val wallTile = WallTile(wallTileImageView as ImageView)
            wallTiles.add(wallTile)

            wallTileImageView.post {
                val location = IntArray(2)
                wallTileImageView.getLocationInWindow(location)
                wallTile.setXPos(location[0])
                wallTile.setYPos(location[1])
                var tileAdded = false
                if(tileMap.isNotEmpty()){
                    val tileRowList = tileMap.filter { p -> p.first == wallTile.getYPos() }
                    if(tileRowList.isNotEmpty()){
                        val tileRow = tileRowList[0]
                        if(tileRow.second.isNotEmpty()) {
                            for(tile in tileRow.second){
                                if(wallTile.getXPos() < tile.getXPos()) {
                                    tileAdded = true
                                    tileRow.second.add(tileRow.second.indexOf(tile), wallTile)
                                    break
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupGround(){
        // Setup ground tiles
        val groundTilesImageViews =
            getViewsByTag(fullScreenView, "groundTile")

        for (groundTileImageView in groundTilesImageViews) {
            val groundTile = GroundTile(groundTileImageView as ImageView)
            groundTiles.add(groundTile)

            groundTileImageView.post {
                val tileLocation = IntArray(2)
                groundTileImageView.getLocationInWindow(tileLocation)
                groundTile.setXPos(tileLocation[0])
                groundTile.setYPos(tileLocation[1])

                var tileAdded = false
                if(tileMap.isNotEmpty()){
                    val tileRowList = tileMap.filter { p -> p.first == groundTile.getYPos() }
                    if(tileRowList.isNotEmpty()){
                        val tileRow = tileRowList[0]
                        if(tileRow.second.isNotEmpty()) {
                            for(tile in tileRow.second){
                                if(groundTile.getXPos() < tile.getXPos()) {
                                    tileAdded = true
                                    tileRow.second.add(tileRow.second.indexOf(tile), groundTile)
                                    break
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
                val playerLocation = IntArray(2)
                groundTile.tileImageView.getLocationInWindow(playerLocation)
                if(groundTile.tileImageView.tag.toString() == "groundTileStart"){
                    setPlayerStart(groundTile)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPlayerStart(groundTile: GroundTile){
        val playerImageView = findViewById<ImageView>(R.id.playerImageView)
        val playerLocation = IntArray(2)
        groundTile.tileImageView.getLocationInWindow(playerLocation)
        player = Player(playerImageView, playerLocation[0], playerLocation[1], groundTile)
        player.getPlayerImageView().translationX = playerLocation[0].toFloat()
        player.getPlayerImageView().translationY = playerLocation[1].toFloat()
        colourTile(groundTile)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if(detector.onTouchEvent(event)){
            true
        } else{
            super.onTouchEvent(event)
        }
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 80
        private val swipeVelocityThreshold = 100

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onFling(
            downEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // Swipe deltas
            val deltaX = moveEvent.x.minus(downEvent.x)
            val deltaY = moveEvent.y.minus(downEvent.y)

            if(player.playerIsMoving)
                return false
            if(!levelStarted) {
                levelStarted = true
                startTime = LocalDateTime.now()
            }
            window.decorView.rootView.performHapticFeedback(12)
            // Check for horizontal or vertical swipe.
            return if(abs(deltaX) > abs(deltaY)) {
                if(abs(deltaX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if(deltaX < 0) {
                        onSwipeLeft()
                    }
                    else {
                        onSwipeRight()
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
                        onSwipeUp()
                    }
                    else {
                        onSwipeDown()
                    }
                    return true
                }
                else{
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeRight() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.getPlayerPosY() }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player.getPlayerGroundTile())
        if(columnIndex < tileRow.second.count()-2) {
            while (tileMap[tileRowIndex].second[columnIndex + 1] is GroundTile) {
                columnIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeLeft() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.getPlayerPosY() }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player.getPlayerGroundTile())
        if(columnIndex > 1) {
            while (tileMap[tileRowIndex].second[columnIndex - 1] is GroundTile) {
                columnIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeDown() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.getPlayerPosY() }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        if(tileRowIndex < tileMap.count()-2) {
            val columnIndex = tileRow.second.indexOf(player.getPlayerGroundTile())
            while (tileMap[tileRowIndex + 1].second[columnIndex] is GroundTile) {
                tileRowIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeUp() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.getPlayerPosY() }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        if(tileRowIndex > 1) {
            val columnIndex = tileRow.second.indexOf(player.getPlayerGroundTile())
            while (tileMap[tileRowIndex - 1].second[columnIndex] is GroundTile) {
                tileRowIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if(crossedTiles.isNotEmpty()){
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun movePlayer(crossedTiles: ArrayList<GroundTile>){
        player.movePlayerPos(crossedTiles)
        for(groundTile in crossedTiles){
            if(!groundTile.getColoured())
                colourTile(groundTile)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun colourTile(groundTile: GroundTile){
        val colourBlindMode = settingPrefs.getBoolean("colorBlind", false)
        groundTile.colourTile()
        groundTile.setColorBlind(colourBlindMode)
        colouredTileCount += 1
        if(colouredTileCount == groundTiles.count())
            levelComplete()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun levelComplete(){
        // update to pass in time
        val endTime = LocalDateTime.now()
        val timeToComplete = timeDifference(startTime, endTime)
        WinDialog(context = this).showWin(timeToComplete, levelId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeDifference(startTime : LocalDateTime, endTime : LocalDateTime) : Int {
        var ms = 0

        if(endTime.dayOfYear != startTime.dayOfYear){
            ms += 1000 * 60 * 60 * 24 * (endTime.dayOfYear - startTime.dayOfYear)
        }
        if(endTime.hour != startTime.hour){
            ms += 1000 * 60 * 60 * (endTime.hour - startTime.hour)
        }
        if(endTime.minute != startTime.minute){
            ms += 1000 * 60 * (endTime.minute - startTime.minute)
        }
        if(endTime.second != startTime.second){
            ms += 1000 * (endTime.second - startTime.second)
        }
        if(endTime.nano != startTime.nano){
            ms += ((endTime.nano - startTime.nano) * 0.000001).toInt()
        }

        return ms
    }

    private fun updatePlayerSkin(currentPlayerSkin: Int){
        val playerImageView = findViewById<ImageView>(R.id.playerImageView)
        playerImageView.setImageResource(currentPlayerSkin)
    }

    private fun updateColorBlindMode(colorBlindMode: Boolean){
        for (tile in groundTiles ){
            if ((tile as GroundTile).getColoured())
                tile.setColorBlind(colorBlindMode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetLevel(){
        colouredTileCount = 0
        for (tile in groundTiles ){
            val groundTile = (tile as GroundTile)
            groundTile.uncolourTile()
            if(groundTile.tileImageView.tag.toString() == "groundTileStart"){
                setPlayerStart(groundTile)
            }
        }
        levelStarted = false
    }
}