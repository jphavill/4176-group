package com.example.csci4176_groupproject.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.databinding.ActivityLevelSelectBinding
import com.example.csci4176_groupproject.fragments.LevelButtonFragment
import com.example.csci4176_groupproject.interfaces.BuyDialogCallback
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.viewModel.StarCountViewModel
import com.google.gson.Gson

class LevelSelectActivity : BaseActivity(), BuyDialogCallback {
    private lateinit var binding: ActivityLevelSelectBinding
    private var pageNumber: Int = 0
    private val perPage: Int = 6
    private var replace: Boolean = false
    private val starCount: StarCountViewModel by viewModels()

    override fun binaryDialogCallback(result: Boolean) {
        if (result) {
            updateButtons()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true
        fullscreenContent = binding.levelSelectFullscreenContent

        updateButtons()

        super.addTopBar("Level Select", "MainMenuActivity")

        starCount.starCount.observe(this) {
            updateStars()
        }
        settingsViewModel.resetLevels.observe(this) {
            updateButtons()
        }
    }

    private fun updateButtons() {
        updateStars()
        updateNavButtons()
        val fullScreenView: ViewGroup = findViewById(R.id.levelSelectFullscreenContent)
        val levelButtons = getViewsByTag(fullScreenView, "levelButton")
        var buttonIndex = pageNumber * perPage
        for (b in levelButtons) {
            val frag = LevelButtonFragment()
            val args = Bundle()
            args.putInt("buttonIndex", buttonIndex)
            frag.arguments = args
            if (replace) {
                supportFragmentManager.beginTransaction().replace(b.id, frag).commit()
            } else {
                supportFragmentManager.beginTransaction().add(b.id, frag).commit()
            }
            buttonIndex += 1
        }
        replace = true
    }

    private fun updateStars() {
        val starCountView = findViewById<TextView>(R.id.starCount)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()
    }

    private fun updateNavButtons() {
        val levels = LevelActivities().levels
        val nextView = findViewById<ImageButton>(R.id.levelsNextButton)
        if (pageNumber < kotlin.math.floor(levels.size / perPage.toDouble())) {
            nextView.setOnClickListener {
                pageNumber++
                updateButtons()
            }
            nextView.visibility = View.VISIBLE
        } else {
            nextView.visibility = View.INVISIBLE
        }
        val backView = findViewById<ImageButton>(R.id.levelsBackButton)
        if (pageNumber > 0) {
            backView.setOnClickListener {
                pageNumber--
                updateButtons()
            }
            backView.visibility = View.VISIBLE

        } else {
            backView.visibility = View.INVISIBLE
        }
    }

}