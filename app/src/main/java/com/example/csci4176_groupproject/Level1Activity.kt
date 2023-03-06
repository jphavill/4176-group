package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.WindowInsets
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.csci4176_groupproject.databinding.ActivityLevel1Binding
import kotlin.math.abs


class Level1Activity : AppCompatActivity() {
    private lateinit var detector: GestureDetectorCompat

    private lateinit var binding: ActivityLevel1Binding
    private lateinit var fullscreenContent: FrameLayout

    private var isFullscreen: Boolean = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevel1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        isFullscreen = true
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.Level1FullscreenContent

        detector = GestureDetectorCompat(this, GestureListener())

        val backToHomeButton = findViewById<ImageButton>(R.id.BackToHomeButton)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            val builder = AlertDialog.Builder(this,R.style.SettingsDialog)
                .create()
            val view = layoutInflater.inflate(R.layout.settings_dialog,null)
            builder.setView(view)

            val  applyButton = view.findViewById<Button>(R.id.applyButton)
            applyButton.setOnClickListener {
                builder.dismiss()
            }
            val  cancelButton = view.findViewById<Button>(R.id.cancelButton)
            cancelButton.setOnClickListener {
                builder.cancel()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()

        }

        hide()
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if(detector.onTouchEvent(event)){
            true
        } else{
            super.onTouchEvent(event)
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {
        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100

        override fun onFling(
            downEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // Swipe deltas
            val deltaX = moveEvent.x.minus(downEvent.x)
            val deltaY = moveEvent.y.minus(downEvent.y)

            // Check for horizontal or vertical swipe.
            return if(abs(deltaX) > abs(deltaY)) {
                if(abs(deltaX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if(deltaX < 0) {
                        this@Level1Activity.onSwipeLeft()
                    }
                    else {
                        this@Level1Activity.onSwipeRight()
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
                        this@Level1Activity.onSwipeUp()
                    }
                    else {
                        this@Level1Activity.onSwipeDown()
                    }
                    return true
                }
                else{
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }

    fun onSwipeRight() {
        Toast.makeText(this, "Swiped Right", Toast.LENGTH_SHORT).show()
        print("Right Swipe")
    }

    fun onSwipeLeft() {
        Toast.makeText(this, "Swiped Left", Toast.LENGTH_SHORT).show()
        print("Left Swipe")
    }

    fun onSwipeDown() {
        Toast.makeText(this, "Swiped Down", Toast.LENGTH_SHORT).show()
        print("Down Swipe")
    }

    fun onSwipeUp() {
        Toast.makeText(this, "Swiped Up", Toast.LENGTH_SHORT).show()
        print("Up Swipe")
    }


}