package com.example.csci4176_groupproject.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import com.example.csci4176_groupproject.Data.levelActivities
import com.example.csci4176_groupproject.Fragments.LevelButtonFragment
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.StarCountViewModel
import com.example.csci4176_groupproject.databinding.ActivityLevelSelectBinding
import com.example.csci4176_groupproject.Dialogs.BuyDialogCallback
import com.example.csci4176_groupproject.Dialogs.SettingsDialog
import com.example.csci4176_groupproject.Dialogs.SettingsDialogCallback
import com.example.csci4176_groupproject.Models.Level
import com.example.csci4176_groupproject.Models.Settings
import com.google.gson.Gson

class LevelSelectActivity : BaseActivity(), BuyDialogCallback, SettingsDialogCallback {
    private lateinit var binding: ActivityLevelSelectBinding
    private var pageNumber: Int = 0
    private val perPage: Int = 6
    private var replace: Boolean = false
    private val starCount: StarCountViewModel by viewModels()


    override fun binaryDialogCallback(result: Boolean){
        if (result){
            updateButtons()
        }
    }

    override fun settingsDialogCallback(settings: Settings){
        val changes = settings.changes

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
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        val resetLevelData = findViewById<Button>(R.id.resetLevels)
        resetLevelData.setOnClickListener {
            resetLevels()
            updateButtons()
        }

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            SettingsDialog(context = this).showSettings(this)
        }

        starCount.starCount.observe(this) {
            updateStars()
        }

        hideAndroidUI()
    }

    private fun resetLevels(){
        for (id in 1 until 11){
            // the last level on the first page, and all subsequent levels, are locked by default
            val tempLevel = Level(id=id, locked = id > perPage-1)
            val gson = Gson()
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            tempLevel.starsEarned = 0
            tempLevel.time = -1
            tempLevel.tried = false
            editor.putString(String.format("level%d", id), gson.toJson(tempLevel))
            editor.putInt("stars", 0)
            editor.apply()
        }
    }

    private fun updateButtons() {
        updateStars()
        updateNavButtons()
        val fullScreenView: ViewGroup = findViewById(R.id.LevelSelectFullscreenContent)
        val levelButtons = getViewsByTag(fullScreenView, "levelButton")
        var buttonIndex = pageNumber*perPage
        for (b in levelButtons){
            val viewreplace = b
            val frag = LevelButtonFragment()
            val args = Bundle()
            args.putInt("buttonIndex",buttonIndex)
            frag.arguments = args
            if (replace){
                supportFragmentManager.beginTransaction().replace(viewreplace.id, frag).commit()
            } else{
                supportFragmentManager.beginTransaction().add(viewreplace.id, frag).commit()
            }
            buttonIndex += 1
        }
        replace = true
    }

    fun updateStars() {
        val starCountView = findViewById<TextView>(R.id.starCount)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()
    }

    private fun updateNavButtons(){
        val levels = levelActivities().levels
        val nextView = findViewById<ImageButton>(R.id.levelsNextButton)
        if (pageNumber < kotlin.math.floor(levels.size / perPage.toDouble())) {
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


}