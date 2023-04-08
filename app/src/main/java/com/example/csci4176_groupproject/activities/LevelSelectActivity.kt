package com.example.csci4176_groupproject.activities

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
import com.example.csci4176_groupproject.viewModels.StarCountViewModel

class LevelSelectActivity : BaseActivity(), BuyDialogCallback {
    private lateinit var binding: ActivityLevelSelectBinding
    private var pageNumber: Int = 0
    private val perPage: Int = 6
    private var replace: Boolean = false
    private val starCount: StarCountViewModel by viewModels()

    override fun buyDialogCallback(result: Boolean) {
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

        // observor to update the starCount when a user purchases a level for example
        starCount.starCount.observe(this) {
            updateStars()
        }
        // this is in addition to the obeservor in BaseActivity that this Activity also inherits
        // this is because the level buttons must live update to reflect this, without requiring
        // the activity to be reloaded
        settingsViewModel.resetLevels.observe(this) {
            updateButtons()
        }
    }

    private fun updateButtons() {
        updateStars()
        updateNavButtons()
        val fullScreenView: ViewGroup = findViewById(R.id.levelSelectFullscreenContent)
        val levelButtons = getViewsByTag(fullScreenView, "levelButton")
        // button index is used to determine what level each button links to
        // therefore the buttons on page 0 need different index's then page 1 etc
        var buttonIndex = pageNumber * perPage
        for (b in levelButtons) {
            // for each button on the page, set the corresponding fragment
            val frag = LevelButtonFragment()
            val args = Bundle()
            args.putInt("buttonIndex", buttonIndex)
            frag.arguments = args
            // if the buttons are being updated after the page has initially been created,
            // ie when a user resets the levels, then replace the existing fragments instead of
            // adding new ones
            if (replace) {
                supportFragmentManager.beginTransaction().replace(b.id, frag).commit()
            } else {
                supportFragmentManager.beginTransaction().add(b.id, frag).commit()
            }
            buttonIndex += 1
        }
        // tracks that fragments have now been added and should therefore be replaced in the future
        replace = true
    }

    private fun updateStars() {
        val starCountView = findViewById<TextView>(R.id.starCount)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()
    }

    private fun updateNavButtons() {
        val levels = LevelActivities().levels
        val nextView = findViewById<ImageButton>(R.id.levelsNextButton)
        // if the pageNumber is less than the total number of levels divided by the number of levels
        // per page, then that means there are more pages. Therefore show the next page button
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
        // if not on the first page then show the previous page button
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