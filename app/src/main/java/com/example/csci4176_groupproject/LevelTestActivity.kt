package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs


class LevelTestActivity : AppCompatActivity() {
    private lateinit var detector: GestureDetectorCompat
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_level_test)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        detector = GestureDetectorCompat(this, GestureListener())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if(detector.onTouchEvent(event)){
            true
        } else{
            super.onTouchEvent(event)
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {
        private val SwipeThreshold = 100
        private val SwipeVelocityThreshold = 100

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
                if(abs(deltaX) > SwipeThreshold && abs(velocityX) > SwipeVelocityThreshold) {
                    if(deltaX < 0) {
                        this@LevelTestActivity.onSwipeLeft()
                    }
                    else {
                        this@LevelTestActivity.onSwipeRight()
                    }
                    return true
                }
                else{
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
            else {
                if(abs(deltaY) > SwipeThreshold && abs(velocityY) > SwipeVelocityThreshold) {
                    if(deltaY < 0) {
                        this@LevelTestActivity.onSwipeUp()
                    }
                    else {
                        this@LevelTestActivity.onSwipeDown()
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