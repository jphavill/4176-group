package com.example.csci4176_groupproject

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.example.csci4176_groupproject.databinding.ActivityLevel10Binding
import com.example.csci4176_groupproject.databinding.ActivityLevel2Binding
import kotlin.math.abs


class Level10Activity : BaseLevel() {
    override val levelId: Int = 10

    private lateinit var binding: ActivityLevel10Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel10Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level10FullscreenContent
        fullScreenView = findViewById(R.id.Level10FullscreenContent)
        super.levelSetup()
    }
}