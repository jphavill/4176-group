package com.example.csci4176_groupproject

import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.Gson

class winDialog(context: Context) : AlertDialog.Builder(context)  {
    /**
     * @param milliseconds the number of miliseconds it took a user to complete a level
     * @param starTime the time in miliseconds a user must beat to earn the first star
     */
    fun showWin(milliseconds: Int, levelId: Int) {
        var starCount = calcStars(milliseconds, levelId)

        val tempLevel = levelData(id=levelId, locked = false)
        val gson = Gson()
        val sp: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)
        var level: levelData = gson.fromJson(sp.getString(String.format("level%d", levelId), gson.toJson(tempLevel)), levelData::class.java)

        val builder = AlertDialog.Builder(context, R.style.SettingsDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.win_dialog, null)
        builder.setView(view)

        // set the seconds
        val timeView = view.findViewById<TextView>(R.id.timeText)
        timeView.text = String.format("Time: %.2fs", milliseconds.toFloat() / 1000)

        // set state of stars
        val stars: List<ImageView> = listOf(view.findViewById(R.id.star1Image), view.findViewById(R.id.star2Image), view.findViewById(R.id.star3Image))
        while(starCount > 0){
            starCount--
            stars[starCount].setImageDrawable(AppCompatResources.getDrawable(context, android.R.drawable.btn_star_big_on))
        }

        // update level data stored in persistent memory
        level = updateLevel(level, starCount, milliseconds)
        val editor: SharedPreferences.Editor = sp.edit()
        val newStars = if (level.starsEarned > starCount) 0 else starCount - level.starsEarned
        editor.putInt("stars", sp.getInt("stars", 0) + newStars)
        editor.putString(String.format("level%d", levelId), gson.toJson(level))
        editor.apply()

        val nextLevelView = view.findViewById<Button>(R.id.nextLevelButton)

        val nextLevel: levelData = gson.fromJson(sp.getString(String.format("level%d", levelId+1), gson.toJson((tempLevel))), levelData::class.java)
        if (levelId >= 10 || nextLevel.locked) {
            nextLevelView.isEnabled = false
            nextLevelView.isClickable = false
        } else {
            nextLevelView.setOnClickListener {
                context.startActivity(getNextLevel(levelId))
                builder.dismiss()
            }
        }


        val mainMenuView = view.findViewById<Button>(R.id.mainMenuButton)
        mainMenuView.setOnClickListener {
            val intent = Intent(context, FullscreenActivity::class.java)
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

    fun updateLevel(l: levelData, sCount: Int, mTime: Int): levelData {
        l.starsEarned = if (l.starsEarned > sCount) l.starsEarned else sCount
        l.time = if (l.time > mTime) mTime else l.time
        return l
    }

    fun getNextLevel(lID: Int): Intent{
        val levels = listOf(Level1Activity::class.java,
            Level2Activity::class.java,
            Level3Activity::class.java,
            Level4Activity::class.java,
            Level5Activity::class.java,
            Level6Activity::class.java,
            Level7Activity::class.java,
            Level8Activity::class.java,
            Level9Activity::class.java,
            Level10Activity::class.java,
        )

        return Intent(context, levels[lID])
    }
}