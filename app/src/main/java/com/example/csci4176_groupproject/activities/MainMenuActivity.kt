//Contributors: Jason Havill, Justin Mackinnon, Suraj Patnaikuni
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

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            SettingsDialog(context = this).showSettings(this)
        }

        val menuButtonAction = findViewById<ImageButton>(R.id.mainMenuLevelSelectButton)
        menuButtonAction.setOnClickListener {
            val intent = Intent(this, LevelSelectActivity::class.java)
            startActivity(intent)
        }

        // The play button takes the user to the first unlocked and unfinished level
        // unfinished means there is no time saved for that level
        // if there are no more unlocked and unfinished levels to take the user to, it goes to the
        // first level
        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener {
            val gson = Gson()
            val levels = LevelActivities().levels
            var found = false
            for (levelIndex in 0..levels.size) {
                val tempLevel = Level(id = levelIndex, locked = true)
                // retrieve the level object from persistent storage
                val level: Level = gson.fromJson(
                    settingPrefs.getString(
                        // levelIndex + 1 was used for simplicity, so that level 1 has an id of level1
                        String.format("level%d", levelIndex + 1),
                        gson.toJson(tempLevel)
                    ), Level::class.java
                )
                // if the time is == -1 then the level has never been finished
                if (level.time == -1 && !level.locked) {
                    // when a level is opened, set tried to true. This is used to determine the color
                    // of the the level in the level select screen.
                    level.tried = true
                    // store the level object back in persistent storage
                    val editor: SharedPreferences.Editor = settingPrefs.edit()
                    editor.putString(String.format("level%d", levelIndex + 1), gson.toJson(level))
                    editor.apply()
                    val intent = Intent(this, levels[levelIndex])
                    startActivity(intent)
                    found = true
                    break
                }
            }
            // if no unfinished and unlocked level is found, start the first level
            if (!found) {
                // since the level by definition has already been finished, level.tried is not updated
                val intent = Intent(this, levels[0])
                startActivity(intent)
            }
        }
        val storeButton = findViewById<ImageButton>(R.id.shoppingCartButton)

        storeButton.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }
        // hide status bar
        hideAndroidUI()
    }

    override fun settingsDialogCallback(settings: Settings) {
        // the Main Menu activity does not use the topBar fragment to house it's settings button
        // This is because it does not have a back button, and it's title is in a different postiion
        // therefore it has it's own settingsDialogCallback within the activity
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