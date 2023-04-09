//Contributors: Jason Havill
package com.example.csci4176_groupproject.dialogs

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.activities.LevelSelectActivity
import com.example.csci4176_groupproject.activities.MainMenuActivity
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.models.Level
import com.google.gson.Gson

class WinDialog(context: Context) : AlertDialog.Builder(context) {
    private val settingPrefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("settingsPrefs", 0)

    fun showWin(milliseconds: Int, levelId: Int) {
        var starCount = calcStars(milliseconds, levelId)

        val tempLevel = Level(id = levelId, locked = false)
        val gson = Gson()

        // retrieve corresponding level object from persistent memory
        var level: Level = gson.fromJson(
            settingPrefs.getString(
                String.format("level%d", levelId),
                gson.toJson(tempLevel)
            ), Level::class.java
        )

        val builder = AlertDialog.Builder(context, R.style.roundedDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.win_dialog, null)
        builder.setView(view)

        // set the seconds
        val timeView = view.findViewById<TextView>(R.id.timeText)
        timeView.text = String.format("Time: %.2fs", milliseconds.toFloat() / 1000)

        // If it's a new best time congragulate the player, otherwise show best time
        // set the seconds
        val bestTimeView = view.findViewById<TextView>(R.id.bestTimeText)
        if (level.time != -1 && level.time < milliseconds) {
            bestTimeView.text = String.format("Best Time: %.2fs", level.time.toFloat() / 1000)
        } else {
            bestTimeView.text = context.getString(R.string.new_best_time)
        }


        // update level data stored in persistent memory
        // stars are only added to a users "bank account" of stars if they are new, meaning the
        // user hasn't earned that star for this level yet
        val newStars = if (level.starsEarned > starCount) 0 else starCount - level.starsEarned
        level = updateLevel(level, starCount, milliseconds)
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        editor.putInt("stars", settingPrefs.getInt("stars", 0) + newStars)
        editor.putString(String.format("level%d", levelId), gson.toJson(level))
        editor.apply()


        val stars: List<ImageView> = listOf(
            view.findViewById(R.id.star1Image),
            view.findViewById(R.id.star2Image),
            view.findViewById(
                R.id.star3Image
            )
        )
        // set state of star icons that show how many stars the user earned
        while (starCount > 0) {
            starCount--
            stars[starCount].setImageDrawable(
                AppCompatResources.getDrawable(
                    context,
                    android.R.drawable.btn_star_big_on
                )
            )
        }

        val nextLevelView = view.findViewById<Button>(R.id.nextLevelButton)

        val nextLevel: Level = gson.fromJson(
            settingPrefs.getString(
                // levelId+1 because levels are indexed at 1 so that level 1 is at levelId 1
                String.format("level%d", levelId + 1),
                gson.toJson((tempLevel))
            ), Level::class.java
        )

        if (levelId >= LevelActivities().levels.size || nextLevel.locked) {
            nextLevelView.isEnabled = false
            nextLevelView.isClickable = false
        } else {
            nextLevelView.setOnClickListener {
                // when a level is opened, set tried to true. This is used to determine the color
                // of the the level in the level select screen.
                nextLevel.tried = true
                editor.putString(String.format("level%d", levelId + 1), gson.toJson(nextLevel))
                editor.apply()
                context.startActivity(Intent(context, LevelActivities().levels[levelId]))
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

        // force the user to use one of the buttons to close the dialog
        builder.setCanceledOnTouchOutside(false)
        // stop the android status bar from appearing when the dailog is open for consistency
        builder.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        builder.show()
    }

    private fun calcStars(userTime: Int, lID: Int): Int {
        // a user earns stars by completing a level faster than a certian time, starTime
        // for every 2 seccond faster then that time that they are, they earn 1 star
        // starTime is dependant on the level number, with higher levels being bigger and harder
        // and therefore having a higher starTime
        val starTime = 5500 + (lID * 1750)
        val perStarTime = 2000
        var tempStar = kotlin.math.floor((starTime - userTime).toDouble() / perStarTime)
        tempStar = if (tempStar < 0) 0.0 else tempStar
        return (if (tempStar > 3) 3.0 else tempStar).toInt()
    }

    private fun updateLevel(l: Level, sCount: Int, mTime: Int): Level {
        // update the level with the new maximum of stars earned
        // and the new minimum of the best time that the user has completed the level in
        l.starsEarned = if (l.starsEarned > sCount) l.starsEarned else sCount
        l.time = if (l.time > mTime || l.time == -1) mTime else l.time
        return l
    }
}