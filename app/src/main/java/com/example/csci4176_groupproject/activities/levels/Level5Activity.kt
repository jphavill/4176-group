//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.activities.levels

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.activities.BaseLevelActivity
import com.example.csci4176_groupproject.databinding.ActivityLevel5Binding

class Level5Activity : BaseLevelActivity() {
    override val levelId: Int = 5
    private lateinit var binding: ActivityLevel5Binding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create the view for this specific level and inflate it
        // then run the actual setup with levelSetup since that is the same for each level
        binding = ActivityLevel5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        fullscreenContent = binding.Level5FullscreenContent
        fullScreenView = findViewById(R.id.Level5FullscreenContent)
        super.levelSetup()
    }
}