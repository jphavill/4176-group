package com.example.csci4176_groupproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.databinding.ActivityMainMenuBinding
import com.example.csci4176_groupproject.dialogs.SettingsDialog
import com.example.csci4176_groupproject.interfaces.SettingsDialogCallback
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.models.SettingChange
import com.example.csci4176_groupproject.models.Settings
import com.google.gson.Gson

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MainMenuActivity : BaseActivity(), SettingsDialogCallback {
    private lateinit var binding: ActivityMainMenuBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        fullscreenContent = binding.MainFullscreenContent
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            SettingsDialog(context = this).showSettings(this)
        }

        val menuButtonAction = findViewById<ImageButton>(R.id.mainMenuLevelSelectButton)
        menuButtonAction.setOnClickListener {
            val intent = Intent(this, LevelSelectActivity::class.java)
            startActivity(intent)
        }

        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            val gson = Gson()
            val levels = LevelActivities().levels
            var found = false
            for (levelIndex in 0..levels.size) {
                val tempLevel = Level(id = levelIndex, locked = true)
                val level: Level = gson.fromJson(
                    settingPrefs.getString(
                        String.format("level%d", levelIndex + 1),
                        gson.toJson(tempLevel)
                    ), Level::class.java
                )
                if (level.time == -1 && !level.locked) {
                    level.tried = true
                    val editor: SharedPreferences.Editor = settingPrefs.edit()
                    editor.putString(String.format("level%d", levelIndex + 1), gson.toJson(level))
                    editor.apply()
                    val intent = Intent(this, levels[levelIndex])
                    startActivity(intent)
                    found = true
                    break
                }
            }
            if (!found) {
                val intent = Intent(this, levels[0])
                startActivity(intent)
            }
        }
        val storeButton = findViewById<ImageButton>(R.id.shoppingCartButton)

        storeButton.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
    }

    override fun settingsDialogCallback(settings: Settings) {
        val changes = settings.changes
        if (changes[SettingChange.Haptics]!!) {
            window.decorView.rootView.isHapticFeedbackEnabled = settings.haptics
        }
        if (changes[SettingChange.ResetLevels]!!) {
            settingsViewModel.setResetLevels(settings.resetLevels)
        }
        if (changes[SettingChange.ResetStore]!!) {
            settingsViewModel.setResetStore(settings.resetStore)
        }
    }
}