package com.example.csci4176_groupproject.Activities.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.Activities.BaseLevelActivity
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel9Binding


class Level9Activity : BaseLevelActivity() {
    override val levelId: Int = 9
    private lateinit var binding: ActivityLevel9Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel9Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level9FullscreenContent
        fullScreenView = findViewById(R.id.Level9FullscreenContent)
        super.levelSetup()
    }
}