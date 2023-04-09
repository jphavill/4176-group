//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.activities.levels

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.activities.BaseLevelActivity
import com.example.csci4176_groupproject.databinding.ActivityLevel2Binding

class Level2Activity : BaseLevelActivity() {
    override val levelId: Int = 2
    private lateinit var binding: ActivityLevel2Binding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // create the view for this specific level and inflate it
        // then run the actual setup with levelSetup since that is the same for each level
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level2FullscreenContent
        fullScreenView = findViewById(R.id.Level2FullscreenContent)
        super.levelSetup()
        // hide status bar
        hideAndroidUI()
    }
}