package com.example.csci4176_groupproject

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.databinding.ActivityLevel6Binding


class Level6Activity : BaseLevel() {
    override val levelId: Int = 6
    private lateinit var binding: ActivityLevel6Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel6Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level6FullscreenContent
        fullScreenView = findViewById(R.id.Level6FullscreenContent)
        super.levelSetup()
    }
}