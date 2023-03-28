package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.content.SharedPreferences
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.*
import com.example.csci4176_groupproject.databinding.ActivityFullscreenBinding
import com.google.gson.Gson
import androidx.appcompat.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : BaseActivity(), settingsDialogCallback {
    private lateinit var binding: ActivityFullscreenBinding

    override fun settingsDialogCallback(settings: settingsData){
        val changes = settings.changes
        if (changes["playerIcon"]!!){
            Log.d("seting change", "changing player icon setting")
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.MainFullscreenContent



        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            settingsDialog(context = this).showSettings(this)
        }

        val menuButtonAction = findViewById<ImageButton>(R.id.menuButton)
        menuButtonAction.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            startActivity(intent)
        }
        hideAndroidUI()

        val playTestButton = findViewById<Button>(R.id.playButton)
        playTestButton.setOnClickListener{


            val gson = Gson()
            val levels = levelActivities().levels
            var found = false
            for (levelIndex in 0..levels.size){
                val tempLevel =  levelData(id=levelIndex, locked = true)
                val level: levelData = gson.fromJson(settingPrefs.getString(String.format("level%d", levelIndex+1), gson.toJson(tempLevel)), levelData::class.java)
                if (level.time == -1 && !level.locked){
                    level.tried = true
                    val editor: SharedPreferences.Editor = settingPrefs.edit()
                    editor.putString(String.format("level%d", levelIndex+1), gson.toJson(level))
                    editor.apply()
                    val intent = Intent(this, levels[levelIndex])
                    startActivity(intent)
                    found = true
                    break
                }
            }
            if (!found){
                val intent = Intent(this, levels[0])
                startActivity(intent)
            }
        }

        hideAndroidUI()
    }
}