//Contributors: Jason Havill
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
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.fragments.TopBarFragment
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.viewModels.SettingsViewModel
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {
    // All activies in the app inherit from this
    var isFullscreen: Boolean = true
    lateinit var fullscreenContent: FrameLayout
    lateinit var settingPrefs: SharedPreferences
    val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // hide status bar
        hideAndroidUI()

        // persistent storage shared by the entire application
        settingPrefs = this.applicationContext.getSharedPreferences("settingsPrefs", 0)
        // haptics are enabled by default, or set to the setting stored in persistent storage
        updateHaptics(settingPrefs.getBoolean("haptics", true))

        // observors to update settings, will be triggered by the TopBar fragment
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
        // controls if haptics are enabled for the entire activity with one setting
        window.decorView.rootView.isHapticFeedbackEnabled = state
    }

    fun addTopBar(title: String, backActivity: String) {
        // adds the TopBar fragment that holds the back button, title and settings button
        val topBar = TopBarFragment()
        val args = Bundle()
        args.putString("title", title)
        // determines what activity the back button will open
        args.putString("backActivity", backActivity)
        topBar.arguments = args
        supportFragmentManager.beginTransaction().add(R.id.topBar, topBar).commit()
    }

    private fun hideAndroidUI() {
        // Hides the android status bar at the top of the screen
        // without doing this, relativeLayouts are offset by the height of the status bar
        // this would make the player character appear in the wrong place
        // since status bars can be different heights, the easiest way to deal with this is to hide it
        supportActionBar?.hide()
        isFullscreen = false

        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }


    fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View> {
        // gets all views in an xml layout that have a certian tag. Order IS maintained
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
        // enable fullscreen mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun resetLevels() {
        // starts at 1 because levels are id'd starting at 1 so level 1 has an id of level1 instead of level0
        for (id in 1  .. LevelActivities().levels.size) {
            // all levels after level 5 are locked by default
            val tempLevel = Level(id = id, locked = id > 5)
            // overwrite each level object in persistent storage to reset them
            val gson = Gson()
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            tempLevel.starsEarned = 0
            tempLevel.time = -1
            tempLevel.tried = false
            editor.putString(String.format("level%d", id), gson.toJson(tempLevel))
            // reset the number of stars the user has earned to 0
            editor.putInt("stars", 0)
            editor.apply()
        }
    }

    private fun resetStore() {
        // overwrite each cosmetic object in persistent storage to reset them
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        for (id in 0 until CosmeticList().itemList.size) {
            val tempCosmetic = CosmeticList().itemList[id]
            editor.putString(String.format("cosmetic%d", id), gson.toJson(tempCosmetic))
        }
        // reset the playerSkin to the default skin, since that will be the only one unlocked
        editor.putInt("playerSkin", CosmeticList().itemList[0].img)
        editor.apply()
    }
}