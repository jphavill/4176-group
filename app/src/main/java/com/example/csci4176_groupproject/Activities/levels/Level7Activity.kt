package com.example.csci4176_groupproject.Activities.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.Activities.BaseLevelActivity
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.databinding.ActivityLevel7Binding



class Level7Activity : BaseLevelActivity() {
    override val levelId: Int = 7
    private lateinit var binding: ActivityLevel7Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel7Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level7FullscreenContent
        fullScreenView = findViewById(R.id.Level7FullscreenContent)
        super.levelSetup()
    }
}
