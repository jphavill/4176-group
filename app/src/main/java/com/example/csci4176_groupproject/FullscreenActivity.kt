package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.csci4176_groupproject.databinding.ActivityFullscreenBinding
import androidx.appcompat.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : BaseActivity() {
    private lateinit var binding: ActivityFullscreenBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.


        val testLevelButton = findViewById<Button>(R.id.ToLevelTest);


        testLevelButton.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            startActivity(intent)

        }
        hideAndroidUI()
        val storeButton = findViewById<Button>(R.id.Store)

        storeButton.setOnClickListener {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }
    }
}