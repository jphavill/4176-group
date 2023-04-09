//Contributors: Jason Havill, Justin MacKinnon
package com.example.csci4176_groupproject.activities

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.GestureDetectorCompat
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.CosmeticList
import com.example.csci4176_groupproject.dialogs.WinDialog
import com.example.csci4176_groupproject.fragments.RestartButtonFragment
import com.example.csci4176_groupproject.models.*
import com.example.csci4176_groupproject.utils.TimeCalcs
import com.example.csci4176_groupproject.viewModels.RestartLevelViewModel
import java.time.LocalDateTime
import kotlin.math.abs

abstract class BaseLevelActivity : BaseActivity() {
    // all level activites inherit from this
    // contains the game logic for playing a level
    abstract val levelId: Int
    private lateinit var player: Player
    private var colouredTileCount = 0
    private val wallTiles: ArrayList<Tile> = ArrayList()
    private val groundTiles: ArrayList<Tile> = ArrayList()
    private val tileMap: ArrayList<Pair<Int, ArrayList<Tile>>> = ArrayList()

    // for timing how long it takes to complete a level
    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var startTime: LocalDateTime
    private var levelStarted = false

    private lateinit var detector: GestureDetectorCompat
    lateinit var fullScreenView: ViewGroup

    private val restartLevelViewModel: RestartLevelViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // observors to update settings, will be triggered by the TopBar fragment
        // these are in addition to the obeservors in BaseActivity that this Activity also inherits
        // this is because there are live updates to the levels specifically that must occur when
        // the settings change. For example updating the players skin immediatly, not only when
        // the Activity is first loaded
        settingsViewModel.colorBlindMode.observe(this) { state ->
            updateColorBlindMode(state)
        }
        settingsViewModel.playerSkin.observe(this) { skin ->
            updatePlayerSkin(skin)
        }
        settingsViewModel.resetStore.observe(this) {
            // reset the player to the default skin when the store is reset since it will be the only
            // unlocked skin
            val currentPlayerSkin = settingPrefs.getInt("playerSkin", CosmeticList().itemList[0].img)
            updatePlayerSkin(currentPlayerSkin)
        }

        restartLevelViewModel.restartLevel.observe(this) {
            resetLevel()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun levelSetup() {
        // level setup is seperate from onCreate because it can only be run after the onCreate of
        // the inheriting Activity. For example, this is run after Level1Activity's onCreate.
        val currentPlayerSkin = settingPrefs.getInt("playerSkin", CosmeticList().itemList[0].img)
        updatePlayerSkin(currentPlayerSkin)
        detector = GestureDetectorCompat(this, GestureListener())

        // the back button in levels return to the LevelSelectActivity
        super.addTopBar("Level $levelId", "LevelSelectActivity")

        supportFragmentManager.beginTransaction().add(R.id.restartButton, RestartButtonFragment())
            .commit()

        setupWalls()
        setupGround()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWalls() {
        // get all wall tile image views
        val wallTilesImageViews =
            getViewsByTag(fullScreenView, "wallTile")

        for (wallTileImageView in wallTilesImageViews) {
            val wallTile = WallTile(wallTileImageView as ImageView)
            wallTiles.add(wallTile)

            postTile(wallTile, wallTileImageView)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupGround() {
        // get all groundTile tile image views
        val groundTilesImageViews =
            getViewsByTag(fullScreenView, "groundTile")

        for (groundTileImageView in groundTilesImageViews) {
            val groundTile = GroundTile(groundTileImageView as ImageView)
            groundTiles.add(groundTile)
            postTile(groundTile, groundTileImageView)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postTile(currTile: Tile, imageView: ImageView){
        imageView.post {
            // syncs the groundTile object's internal location with the location of the imageView
            val tileLocation = IntArray(2)
            imageView.getLocationInWindow(tileLocation)
            currTile.xPos = tileLocation[0]
            currTile.yPos = tileLocation[1]

            var tileAdded = false
            // position the ground tile within the tileMap
            if (tileMap.isNotEmpty()) {
                val tileRowList = tileMap.filter { p -> p.first == currTile.yPos }
                if (tileRowList.isNotEmpty()) {
                    val tileRow = tileRowList[0]
                    if (tileRow.second.isNotEmpty()) {
                        for (tile in tileRow.second) {
                            if (currTile.xPos < tile.xPos) {
                                tileAdded = true
                                tileRow.second.add(tileRow.second.indexOf(tile), currTile)
                                break
                            }
                        }
                        if (!tileAdded) {
                            tileRow.second.add(currTile)
                        }
                    }
                } else {
                    tileMap.add(Pair(currTile.yPos, ArrayList()))
                    val tileRow = tileMap.filter { p -> p.first == currTile.yPos }[0]
                    tileRow.second.add(currTile)
                }
            } else {
                tileMap.add(Pair(currTile.yPos, ArrayList()))
                tileMap[0].second.add(currTile)
            }
            // if the tile is the tile where the player starts, set the players location
            // to that tile
            val playerLocation = IntArray(2)
            currTile.tileImageView.getLocationInWindow(playerLocation)
            if (currTile.tileImageView.tag.toString() == "groundTileStart") {
                setPlayerStart(currTile as GroundTile)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPlayerStart(groundTile: GroundTile) {
        // given a ground tile set the player to the location on the scren of that ground tile
        val playerImageView = findViewById<ImageView>(R.id.playerImageView)
        val playerLocation = IntArray(2)
        groundTile.tileImageView.getLocationInWindow(playerLocation)
        player = Player(playerImageView, playerLocation[0], playerLocation[1], groundTile)
        player.playerImageView.translationX = playerLocation[0].toFloat()
        player.playerImageView.translationY = playerLocation[1].toFloat()
        // color the tile since the player is standing on it
        colourTile(groundTile)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
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

            // if the player is already moving, ignore gestures until they come to a stop
            if (player.playerIsMoving)
                return false
            // start the timer on the first gesture
            if (!levelStarted) {
                levelStarted = true
                startTime = LocalDateTime.now()
            }
            // provide haptic feedback to show a gesture will cause the player to move
            // this is done AFTER a gesture would be rejected for the player already being in motion
            // meaning the user should be able to easily tell if their gesture will result in new
            // movement or not
            window.decorView.rootView.performHapticFeedback(12)
            // Check for horizontal or vertical swipe, and that the swipe was longer and faster
            // then the respective thresholds
            return if (abs(deltaX) > abs(deltaY)) {
                if (abs(deltaX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (deltaX < 0) {
                        onSwipeLeft()
                    } else {
                        onSwipeRight()
                    }
                    return true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                if (abs(deltaY) > swipeThreshold && abs(velocityY) > swipeVelocityThreshold) {
                    if (deltaY < 0) {
                        onSwipeUp()
                    } else {
                        onSwipeDown()
                    }
                    return true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeRight() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.playerPosY }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player.playerGroundTile)
        // -2 because the last tile will be a wall, if the player is in the second last tile then
        // they can't move down into that wall
        if (columnIndex < tileRow.second.count() - 2) {
            // column+1 to check if the NEXT tile to the right is a ground tile the player can move to
            while (tileMap[tileRowIndex].second[columnIndex + 1] is GroundTile) {
                columnIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if (crossedTiles.isNotEmpty()) {
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeLeft() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.playerPosY }[0]
        val tileRowIndex = tileMap.indexOf(tileRow)
        var columnIndex = tileRow.second.indexOf(player.playerGroundTile)
        if (columnIndex > 1) {
            // column-1 to check if the NEXT tile to the left  is a ground tile the player can move to
            while (tileMap[tileRowIndex].second[columnIndex - 1] is GroundTile) {
                columnIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if (crossedTiles.isNotEmpty()) {
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeDown() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.playerPosY }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        // -2 because the last tile will be a wall, if the player is in the second last tile then
        // they can't move down into that wall
        if (tileRowIndex < tileMap.count() - 2) {
            val columnIndex = tileRow.second.indexOf(player.playerGroundTile)
            // row+1 to check if the NEXT tile to the bottom is a ground tile the player can move to
            while (tileMap[tileRowIndex + 1].second[columnIndex] is GroundTile) {
                tileRowIndex += 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if (crossedTiles.isNotEmpty()) {
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSwipeUp() {
        val crossedTiles: ArrayList<GroundTile> = ArrayList()
        val tileRow = tileMap.filter { p -> p.first == player.playerPosY }[0]
        var tileRowIndex = tileMap.indexOf(tileRow)
        if (tileRowIndex > 1) {
            val columnIndex = tileRow.second.indexOf(player.playerGroundTile)
            // row-1 to check if the NEXT tile to the bottom is a ground tile the player can move to
            while (tileMap[tileRowIndex - 1].second[columnIndex] is GroundTile) {
                tileRowIndex -= 1
                crossedTiles.add((tileMap[tileRowIndex].second[columnIndex]) as GroundTile)
            }
            if (crossedTiles.isNotEmpty()) {
                movePlayer(crossedTiles)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun movePlayer(crossedTiles: ArrayList<GroundTile>) {
        // animates the player across the tiles
        player.movePlayerPos(crossedTiles)
        for (groundTile in crossedTiles) {
            if (!groundTile.coloured)
                colourTile(groundTile)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun colourTile(groundTile: GroundTile) {
        // update the coloured state, and check if colourBlind mode is on
        val colourBlindMode = settingPrefs.getBoolean("colorBlind", false)
        groundTile.colourTile()
        groundTile.setColorBlind(colourBlindMode)
        // if all tiles are coloured the user has beaten the level
        colouredTileCount += 1
        if (colouredTileCount == groundTiles.count())
            levelComplete()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun levelComplete() {
        val endTime = LocalDateTime.now()
        val timeToComplete = TimeCalcs().timeDifference(startTime, endTime)
        WinDialog(context = this).showWin(timeToComplete, levelId)
    }



    private fun updatePlayerSkin(currentPlayerSkin: Int) {
        val playerImageView = findViewById<ImageView>(R.id.playerImageView)
        playerImageView.setImageResource(currentPlayerSkin)
    }

    private fun updateColorBlindMode(colorBlindMode: Boolean) {
        // set all coloured tiles to colorBlindMode when it changes part way through playing a level
        for (tile in groundTiles) {
            if ((tile as GroundTile).coloured)
                tile.setColorBlind(colorBlindMode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetLevel() {
        // restart the level with the player back at the start, tile uncoloured, and the
        colouredTileCount = 0
        for (tile in groundTiles) {
            val groundTile = (tile as GroundTile)
            groundTile.uncolourTile()
            if (groundTile.tileImageView.tag.toString() == "groundTileStart") {
                setPlayerStart(groundTile)
            }
        }
        // timer stopped and reset, waiting to start when the user moves the player
        levelStarted = false
    }
}