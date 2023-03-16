package com.example.csci4176_groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.csci4176_groupproject.databinding.ActivityLevelSelectBinding
import com.google.gson.Gson
import org.w3c.dom.Text
import java.lang.Math.floor

class LevelSelect : AppCompatActivity() {
    private lateinit var binding: ActivityLevelSelectBinding
    private lateinit var fullscreenContent: FrameLayout
    private var pageNumber: Int = 0

    private var isFullscreen: Boolean = true
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
        }

        val settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)

        val starCountView = findViewById<TextView>(R.id.starCount)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            settingsDialog(context = this).showSettings()
        }

        hideAndroidUI()
    }

    private fun resetLevels(){
        for (id in 1 until 11){
            val tempLevel = levelData(id=id, locked = false)
            val gson = Gson()
            val settingPrefs: SharedPreferences = this.applicationContext.getSharedPreferences("settingsPrefs", 0)
            val editor: SharedPreferences.Editor = settingPrefs.edit()
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
        var buttonIndex = pageNumber*6
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
                // levels on the first page are unlocked by default
                val tempLevel =  levelData(id=buttonIndex, locked = pageNumber != 0)

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

                    button.setOnClickListener {
                        unlockLevel()
                    }
                } else {
                    button.text = String.format("Level %d", displayIndex)
                    button.setOnClickListener {
                        val intent = Intent(this, levels[intentIndex])
                        startActivity(intent)
                    }
                }
            }
            buttonIndex += 1
        }
    }

    private fun updateStars(levelStars: List<View>, buttonIndex: Int, level: levelData, hide: Boolean = false) {
        val firstStar = (buttonIndex%6) * 3
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
        if (pageNumber < floor(levels.size / 6.0)) {
            nextView.setOnClickListener {
                pageNumber++
                updateButtons()
            }
            nextView.isEnabled = true
            nextView.isClickable = true
        } else {
            nextView.isEnabled = false
            nextView.isClickable = false
        }
        val backView = findViewById<ImageButton>(R.id.levelsBackButton)
        if (pageNumber > 0) {
            backView.setOnClickListener {
                pageNumber--
                updateButtons()}
            backView.isEnabled = true
            backView.isClickable = true

        } else {
            backView.isEnabled = false
            backView.isClickable = false
        }
    }

    private fun unlockLevel(){

    }
}