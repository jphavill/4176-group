package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.*
import com.example.csci4176_groupproject.databinding.ActivityFullscreenBinding
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

        val testLevelButton = findViewById<Button>(R.id.ToLevelTest);

        testLevelButton.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            startActivity(intent)
        }

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
    }
}