package com.example.csci4176_groupproject


import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.databinding.ActivityLevel1Binding
import com.example.csci4176_groupproject.databinding.ActivityLevel2Binding
import com.example.csci4176_groupproject.databinding.ActivityLevel3Binding

class Level2Activity : BaseLevel() {
    override val levelId: Int = 2
    private lateinit var binding: ActivityLevel2Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level2FullscreenContent
        fullScreenView = findViewById(R.id.Level2FullscreenContent)
        super.levelSetup()
    }
}