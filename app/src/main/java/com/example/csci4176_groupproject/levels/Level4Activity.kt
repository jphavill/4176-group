package com.example.csci4176_groupproject.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.BaseLevel
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel4Binding


class Level4Activity : BaseLevel() {
    override val levelId: Int = 4
    private lateinit var binding: ActivityLevel4Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level4FullscreenContent
        fullScreenView = findViewById(R.id.Level4FullscreenContent)
        super.levelSetup()
    }
}