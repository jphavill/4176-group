package com.example.csci4176_groupproject.levels

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.BaseLevel
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel5Binding


class Level5Activity : BaseLevel() {
    override val levelId: Int = 5
    private lateinit var binding: ActivityLevel5Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level5FullscreenContent
        fullScreenView = findViewById(R.id.Level5FullscreenContent)
        super.levelSetup()
    }
}