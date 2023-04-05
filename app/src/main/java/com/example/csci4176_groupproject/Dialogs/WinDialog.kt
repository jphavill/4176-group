package com.example.csci4176_groupproject.Dialogs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.example.csci4176_groupproject.Models.Level
import com.example.csci4176_groupproject.Activities.LevelSelectActivity
import com.example.csci4176_groupproject.Activities.MainMenuActivity
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.Data.levelActivities
import com.google.gson.Gson

class WinDialog(context: Context) : AlertDialog.Builder(context)  {
    /**
     * @param milliseconds the number of miliseconds it took a user to complete a level
     * @param starTime the time in miliseconds a user must beat to earn the first star
     */
    private val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)
    fun showWin(milliseconds: Int, levelId: Int) {
        var starCount = calcStars(milliseconds, levelId)

        val tempLevel = Level(id=levelId, locked = false)
        val gson = Gson()

        var level: Level = gson.fromJson(settingPrefs.getString(String.format("level%d", levelId), gson.toJson(tempLevel)), Level::class.java)

        val builder = AlertDialog.Builder(context, R.style.SettingsDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.win_dialog, null)
        builder.setView(view)

        // set the seconds
        val timeView = view.findViewById<TextView>(R.id.purchaseText)
        timeView.text = String.format("Time: %.2fs", milliseconds.toFloat() / 1000)



        // update level data stored in persistent memory
        val newStars = if (level.starsEarned > starCount) 0 else starCount - level.starsEarned
        level = updateLevel(level, starCount, milliseconds)
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        editor.putInt("stars", settingPrefs.getInt("stars", 0) + newStars)
        editor.putString(String.format("level%d", levelId), gson.toJson(level))
        editor.apply()

        // set state of stars
        val stars: List<ImageView> = listOf(view.findViewById(R.id.star1Image), view.findViewById(R.id.star2Image), view.findViewById(
            R.id.star3Image
        ))
        while(starCount > 0){
            starCount--
            stars[starCount].setImageDrawable(AppCompatResources.getDrawable(context, android.R.drawable.btn_star_big_on))
        }

        val nextLevelView = view.findViewById<Button>(R.id.nextLevelButton)

        val nextLevel: Level = gson.fromJson(settingPrefs.getString(String.format("level%d", levelId+1), gson.toJson((tempLevel))), Level::class.java)
        if (levelId >= 10 || nextLevel.locked) {
            nextLevelView.isEnabled = false
            nextLevelView.isClickable = false
        } else {
            nextLevelView.setOnClickListener {
                nextLevel.tried = true
                editor.putString(String.format("level%d", levelId+1), gson.toJson(nextLevel))
                editor.apply()
                context.startActivity(Intent(context, levelActivities().levels[levelId]))
                builder.dismiss()
            }
        }

        val mainMenuView = view.findViewById<Button>(R.id.mainMenuButton)
        mainMenuView.setOnClickListener {
            val intent = Intent(context, MainMenuActivity::class.java)
            context.startActivity(intent)
            builder.dismiss()
        }

        val levelSelectView = view.findViewById<Button>(R.id.levelSelectButton)
        levelSelectView.setOnClickListener {
            val intent = Intent(context, LevelSelectActivity::class.java)
            context.startActivity(intent)
            builder.dismiss()
        }

    //  the user must hit either the cancel or apply button to close the dialog
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    // calculate how many stars the user earned
    fun calcStars(userTime: Int, lID: Int): Int {
        val starTime = 8000 + (lID * 1000)
        val perStarTime = 2000
        var tempStar = kotlin.math.floor((starTime - userTime).toDouble() / perStarTime)
        tempStar = if (tempStar < 0) 0.0 else tempStar
        return (if (tempStar > 3) 3.0 else tempStar).toInt()
    }

    fun updateLevel(l: Level, sCount: Int, mTime: Int): Level {
        l.starsEarned = if (l.starsEarned > sCount) l.starsEarned else sCount
        l.time = if (l.time > mTime || l.time == -1) mTime else l.time
        return l
    }
}