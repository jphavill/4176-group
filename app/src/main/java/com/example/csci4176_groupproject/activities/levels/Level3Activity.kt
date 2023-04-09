//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.activities.levels

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.activities.BaseLevelActivity
import com.example.csci4176_groupproject.databinding.ActivityLevel3Binding

class Level3Activity : BaseLevelActivity() {
    override val levelId: Int = 3
    private lateinit var binding: ActivityLevel3Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create the view for this specific level and inflate it
        // then run the actual setup with levelSetup since that is the same for each level
        binding = ActivityLevel3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level3FullscreenContent
        fullScreenView = findViewById(R.id.Level3FullscreenContent)
        super.levelSetup()
    }
}