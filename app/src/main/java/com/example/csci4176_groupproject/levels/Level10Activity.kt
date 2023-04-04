package com.example.csci4176_groupproject.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.BaseLevel
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel10Binding


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