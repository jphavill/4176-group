package com.example.csci4176_groupproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import com.example.csci4176_groupproject.databinding.ActivityLevelSelectBinding
import java.lang.Math.floor

class LevelSelect : AppCompatActivity() {
    private lateinit var binding: ActivityLevelSelectBinding
    private lateinit var fullscreenContent: FrameLayout
    private var pageNumber: Int = 0

    private var isFullscreen: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true
        fullscreenContent = binding.LevelSelectFullscreenContent

        val fullScreenView: ViewGroup = findViewById(R.id.LevelSelectFullscreenContent)
        val levelButtons = getViewsByTag(fullScreenView, "levelButton")

        val levels = levelActivities().levels

        var buttonIndex = pageNumber*6
        for (b in levelButtons){
            val button = b as Button
            if (buttonIndex >= levels.size) {
                button.visibility = View.INVISIBLE
                button.isEnabled = false
                button.isClickable = false
            } else {
                button.visibility = View.VISIBLE
                button.isEnabled = true
                button.isClickable = true

                val displayIndex = buttonIndex+1
                val intentIndex = buttonIndex
                button.text = String.format("Level %d", displayIndex)
                button.setOnClickListener {
                    val intent = Intent(this, levels[intentIndex])
                    startActivity(intent)
                }
            }
            buttonIndex += 1
        }

        val nextView = findViewById<ImageButton>(R.id.levelsNextButton)
        if (pageNumber < floor(levels.size / 6.0)) {
            nextView.setOnClickListener { pageNumber++ }
            nextView.isEnabled = true
            nextView.isClickable = true
        } else {
            nextView.isEnabled = false
            nextView.isClickable = false
        }
        val backView = findViewById<ImageButton>(R.id.levelsBackButton)
        if (pageNumber > 0) {
            backView.setOnClickListener { pageNumber-- }
            backView.isEnabled = true
            backView.isClickable = true
        } else {
            backView.isEnabled = false
            backView.isClickable = false
        }


    }

    private fun getViewsByTag(root: ViewGroup, tag: String): List<View> {
        val views: ArrayList<View> = ArrayList()
        val childCount = root.childCount

        for (i in 0 until childCount) {
            val child: View = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag)!!)
            }
            if (child.tag != null && child.tag.toString().contains(tag)) {
                views.add(child)
            }
        }
        return views
    }
}