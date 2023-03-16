package com.example.csci4176_groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.csci4176_groupproject.databinding.ActivityLevelSelectBinding
import com.google.gson.Gson
import java.lang.Math.floor

class LevelSelect : AppCompatActivity(), unlockDialogCallback {
    private lateinit var binding: ActivityLevelSelectBinding
    private lateinit var fullscreenContent: FrameLayout
    private var pageNumber: Int = 0
    private val perPage: Int = 6

    private var isFullscreen: Boolean = true

    override fun unlockDialogCallBack(result: Boolean){
        if (result){
            updateButtons()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true
        fullscreenContent = binding.LevelSelectFullscreenContent

        updateButtons()
        val backToHomeButton = findViewById<ImageButton>(R.id.BackToHomeButton)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }

        val resetLevelData = findViewById<Button>(R.id.resetLevels)
        resetLevelData.setOnClickListener {
            resetLevels()
            updateButtons()
        }

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            settingsDialog(context = this).showSettings()
        }

        hideAndroidUI()
    }

    private fun resetLevels(){
        for (id in 1 until 11){
            // the last level on the first page, and all subsequent levels, are locked by default
            val tempLevel = levelData(id=id, locked = id > perPage-1)
            val gson = Gson()
            val settingPrefs: SharedPreferences = this.applicationContext.getSharedPreferences("settingsPrefs", 0)
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            tempLevel.starsEarned = 0
            tempLevel.time = -1
            tempLevel.tried = false
            editor.putString(String.format("level%d", id), gson.toJson(tempLevel))
            editor.putInt("stars", 0)
            editor.apply()
        }
    }

    private fun getViewsByTag(root: ViewGroup, tag: String): List<View> {
        val views: ArrayList<View> = ArrayList()
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

    private fun updateButtons() {
        updateNavButtons()
        val fullScreenView: ViewGroup = findViewById(R.id.LevelSelectFullscreenContent)
        val levelButtons = getViewsByTag(fullScreenView, "levelButton")
        val levelStars = getViewsByTag(fullScreenView, "levelStar")
        val levels = levelActivities().levels
        var buttonIndex = pageNumber*perPage
        for (b in levelButtons){
            val button = b as Button
            if (buttonIndex >= levels.size) {
                button.visibility = View.INVISIBLE
                button.isEnabled = false
                button.isClickable = false

                val tempLevel =  levelData(id=buttonIndex, locked = pageNumber != 0)
                updateStars(levelStars, buttonIndex, tempLevel, hide=true)

            } else {

                val settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)
                // the last level on the first page, and all subsequent levels, are locked by default
                val tempLevel =  levelData(id=buttonIndex, locked = buttonIndex > perPage-1)

                val gson = Gson()
                val level: levelData = gson.fromJson(settingPrefs.getString(String.format("level%d", buttonIndex+1), gson.toJson(tempLevel)), levelData::class.java)

                button.visibility = View.VISIBLE
                button.isEnabled = true
                button.isClickable = true

                updateStars(levelStars, buttonIndex, level)

                val displayIndex = buttonIndex+1
                val intentIndex = buttonIndex
                if (level.locked){
                    button.text = ""
                    button.setBackgroundResource(R.drawable.lock)
                    button.background.setTint(ContextCompat.getColor(this, R.color.black))
                    button.setOnClickListener {
                        unlockLevel(level.id)
                        updateButtons()
                    }
                } else {
                    button.text = String.format("Level %d", displayIndex)
                    button.setBackgroundResource(android.R.drawable.btn_default)
                    if (level.tried){
                        if (level.time < 0){
                            button.background.setTintList(ContextCompat.getColorStateList(this, R.color.red))
                        } else {
                            button.background.setTintList(ContextCompat.getColorStateList(this, R.color.green))
                        }
                    } else {
                        button.background.setTintList(ContextCompat.getColorStateList(this, R.color.light_grey))
                    }
                    button.setOnClickListener {
                        level.tried = true
                        val editor: SharedPreferences.Editor = settingPrefs.edit()
                        editor.putString(String.format("level%d", level.id), gson.toJson(level))
                        editor.apply()
                        val intent = Intent(this, levels[intentIndex])
                        startActivity(intent)
                    }
                }
            }
            buttonIndex += 1
        }
    }

    private fun updateStars(levelStars: List<View>, buttonIndex: Int, level: levelData, hide: Boolean = false) {
        val starCountView = findViewById<TextView>(R.id.starCount)
        val settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()

        val firstStar = (buttonIndex%perPage) * 3
        val stars = levelStars.subList(firstStar, firstStar+3)

        var starIndex = 0
        for (s in stars){
            val star = s as ImageView
            if (!hide && starIndex < level.starsEarned) {
                star.visibility = View.VISIBLE
            } else {
                star.visibility = View.INVISIBLE
            }
            starIndex += 1
        }
    }

    private fun updateNavButtons(){
        val levels = levelActivities().levels
        val nextView = findViewById<ImageButton>(R.id.levelsNextButton)
        if (pageNumber < floor(levels.size / perPage.toDouble())) {
            nextView.setOnClickListener {
                pageNumber++
                updateButtons()
            }
            nextView.visibility = View.VISIBLE
        } else {
            nextView.visibility = View.INVISIBLE
        }
        val backView = findViewById<ImageButton>(R.id.levelsBackButton)
        if (pageNumber > 0) {
            backView.setOnClickListener {
                pageNumber--
                updateButtons()}
            backView.visibility = View.VISIBLE

        } else {
            backView.visibility = View.INVISIBLE
        }
    }

    private fun unlockLevel(levelId: Int){
        unlockDialog(context = this).showUnlock(levelId, this)
    }
}