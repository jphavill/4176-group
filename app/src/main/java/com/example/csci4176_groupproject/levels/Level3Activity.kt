package com.example.csci4176_groupproject.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.BaseLevel
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel3Binding


class Level3Activity : BaseLevel() {
    override val levelId: Int = 3
    private lateinit var binding: ActivityLevel3Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level3FullscreenContent
        fullScreenView = findViewById(R.id.Level3FullscreenContent)
        super.levelSetup()
    }
}