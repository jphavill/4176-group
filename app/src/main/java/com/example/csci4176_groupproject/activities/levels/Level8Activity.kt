package com.example.csci4176_groupproject.activities.levels


import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.activities.BaseLevelActivity
import com.example.csci4176_groupproject.databinding.ActivityLevel8Binding


class Level8Activity : BaseLevelActivity() {
    override val levelId: Int = 8
    private lateinit var binding: ActivityLevel8Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel8Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level8FullscreenContent
        fullScreenView = findViewById(R.id.Level8FullscreenContent)
        super.levelSetup()
    }
}