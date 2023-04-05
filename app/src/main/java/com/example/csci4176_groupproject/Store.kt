package com.example.csci4176_groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ImageButton
import androidx.activity.viewModels
import com.example.csci4176_groupproject.databinding.ActivityStoreBinding
import com.google.gson.Gson


class Store : BaseActivity(), binaryDialogCallback, settingsDialogCallback {
    private lateinit var binding: ActivityStoreBinding
    private lateinit var starsTextView: TextView
    private var replace: Boolean = false
    private val starCount: StarCountViewModel by viewModels()
    override fun binaryDialogCallback(result: Boolean){
        if (result){
            updateButtons()
        }
    }

    override fun settingsDialogCallback(settings: settingsData){
        val changes = settings.changes
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true
        fullscreenContent = binding.LevelSelectFullscreenContent
        setContentView(binding.root)

        val backToHomeButton = findViewById<ImageButton>(R.id.BackToHomeButton)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

        val resetLevelData = findViewById<Button>(R.id.resetLevels)
        resetLevelData.setOnClickListener {
            resetStore()
            updateButtons()
        }

        val settingsButton = findViewById<ImageButton>(R.id.SettingsButton)
        settingsButton.setOnClickListener {
            settingsDialog(context = this).showSettings(this)
        }

        starsTextView = findViewById(R.id.starCount)

        settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)

        starCount.starCount.observe(this) {
            updateStars()
        }
        updateButtons()
    }

    private fun updateButtons() {
        updateStars()
        val fullScreenView: ViewGroup = findViewById(R.id.LevelSelectFullscreenContent)
        val cosmeticButtons = getViewsByTag(fullScreenView, "cosmeticButton")
        // start at 1 since 0 index is the default skin which is not in the store
        var buttonIndex = 1
        for (b in cosmeticButtons){
            val viewreplace = b
            val frag = CosmeticButton()
            val args = Bundle()
            args.putInt("cosmeticId",buttonIndex)
            frag.arguments = args
            if (replace){
                supportFragmentManager.beginTransaction().replace(viewreplace.id, frag).commit()
            } else{
                supportFragmentManager.beginTransaction().add(viewreplace.id, frag).commit()
            }
            buttonIndex += 1
        }
        replace = true
    }

    fun updateStars() {
        val starCountView = findViewById<TextView>(R.id.starCount)
        starCountView.text = settingPrefs.getInt("stars", 0).toString()
    }

    private fun resetStore(){
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        for (id in 0 until 4){
            val tempCosmetic = CosmeticList().itemList[id]
            editor.putString(String.format("cosmetic%d", id), gson.toJson(tempCosmetic))
        }
        editor.putInt("playerSkin", CosmeticList().itemList[0].img)
        editor.apply()
    }
}





