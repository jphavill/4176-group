package com.example.csci4176_groupproject

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.databinding.ActivityLevel1Binding

class Level1Activity : BaseLevel() {
    override val levelId: Int = 1
    private lateinit var binding: ActivityLevel1Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level1FullscreenContent
        fullScreenView = findViewById(R.id.Level1FullscreenContent)
        super.levelSetup()
    }
}