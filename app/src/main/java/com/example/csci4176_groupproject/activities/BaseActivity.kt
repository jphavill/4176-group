package com.example.csci4176_groupproject.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.CosmeticList
import com.example.csci4176_groupproject.fragments.TopBarFragment
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.viewModel.SettingsViewModel
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {
    var isFullscreen: Boolean = true
    lateinit var fullscreenContent: FrameLayout
    lateinit var settingPrefs: SharedPreferences
    val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideAndroidUI()

        settingPrefs = this.applicationContext.getSharedPreferences("settingsPrefs", 0)
        updateHaptics(settingPrefs.getBoolean("haptics", true))

        settingsViewModel.haptics.observe(this) { state ->
            updateHaptics(state)
        }
        settingsViewModel.resetStore.observe(this) {
            resetStore()
        }
        settingsViewModel.resetLevels.observe(this) {
            resetLevels()
        }
    }

    private fun updateHaptics(state: Boolean) {
        window.decorView.rootView.isHapticFeedbackEnabled = state
    }

    fun addTopBar(title: String, backActivity: String) {
        val topBar = TopBarFragment()
        val args = Bundle()
        args.putString("title", title)
        args.putString("backActivity", backActivity)
        topBar.arguments = args
        supportFragmentManager.beginTransaction().add(R.id.topBar, topBar).commit()
    }

    private fun hideAndroidUI() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }


    fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View> {
        val views: ArrayList<View> = ArrayList()
        val childCount = root.childCount

        for (i in 0 until childCount) {
            val child: View = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }
            if (child.tag != null && child.tag.toString().contains(tag)) {
                views.add(child)
            }
        }
        return views
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    fun resetLevels() {
        for (id in 1 until 11) {
            // the last level on the first page, and all subsequent levels, are locked by default
            val tempLevel = Level(id = id, locked = id > 5)
            val gson = Gson()
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            tempLevel.starsEarned = 0
            tempLevel.time = -1
            tempLevel.tried = false
            editor.putString(String.format("level%d", id), gson.toJson(tempLevel))
            editor.putInt("stars", 0)
            editor.apply()
        }
    }

    fun resetStore() {
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        for (id in 0 until CosmeticList().itemList.size) {
            val tempCosmetic = CosmeticList().itemList[id]
            editor.putString(String.format("cosmetic%d", id), gson.toJson(tempCosmetic))
        }
        editor.putInt("playerSkin", CosmeticList().itemList[0].img)
        editor.apply()
    }
}