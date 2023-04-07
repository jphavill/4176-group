package com.example.csci4176_groupproject.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.example.csci4176_groupproject.fragments.CosmeticButtonFragment
import com.example.csci4176_groupproject.data.CosmeticList
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.viewModel.StarCountViewModel
import com.example.csci4176_groupproject.databinding.ActivityStoreBinding
import com.example.csci4176_groupproject.interfaces.BuyDialogCallback
import com.google.gson.Gson


class StoreActivity : BaseActivity(), BuyDialogCallback {
    private lateinit var binding: ActivityStoreBinding
    private lateinit var starsTextView: TextView
    private var replace: Boolean = false
    private val starCount: StarCountViewModel by viewModels()
    override fun binaryDialogCallback(result: Boolean){
        if (result){
            updateButtons()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true
        fullscreenContent = binding.storeFullscreenContent
        setContentView(binding.root)

        super.addTopBar("Store", "MainMenuActivity")

        // used for development testing
        val resetLevelData = findViewById<Button>(R.id.resetLevels)
        resetLevelData.setOnClickListener {
            resetStore()
            updateButtons()
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
        val fullScreenView: ViewGroup = findViewById(R.id.storeFullscreenContent)
        val cosmeticButtons = getViewsByTag(fullScreenView, "cosmeticButton")
        // start at 1 since 0 index is the default skin which is not in the store
        var buttonIndex = 1
        for (b in cosmeticButtons){
            val frag = CosmeticButtonFragment()
            val args = Bundle()
            args.putInt("cosmeticId",buttonIndex)
            frag.arguments = args
            if (replace){
                supportFragmentManager.beginTransaction().replace(b.id, frag).commit()
            } else{
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

    private fun resetStore(){
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        for (id in 0 until CosmeticList().itemList.size){
            val tempCosmetic = CosmeticList().itemList[id]
            editor.putString(String.format("cosmetic%d", id), gson.toJson(tempCosmetic))
        }
        editor.putInt("playerSkin", CosmeticList().itemList[0].img)
        editor.apply()
    }
}





