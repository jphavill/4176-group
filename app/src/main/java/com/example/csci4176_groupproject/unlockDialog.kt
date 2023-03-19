package com.example.csci4176_groupproject

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson

class unlockDialog(context: Context) : AlertDialog.Builder(context) {
    private val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)

    /**
     * @param milliseconds the number of miliseconds it took a user to complete a level
     * @param starTime the time in miliseconds a user must beat to earn the first star
     */
    fun showUnlock(levelId: Int, callback: dialogCallback){
        val builder = AlertDialog.Builder(context, R.style.SettingsDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.unlock_dialog, null)
        builder.setView(view)

        var totalStars = settingPrefs.getInt("stars", 0)
        val cost = 1

        val tempLevel =  levelData(id=levelId, locked = true)
        val gson = Gson()
        val level: levelData = gson.fromJson(settingPrefs.getString(String.format("level%d", levelId), gson.toJson(tempLevel)), levelData::class.java)


        val unlockButton = view.findViewById<Button>(R.id.unlockButton)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseText)
        // costs 1 star to purchase
        if (cost <= totalStars){
            unlockButton.setOnClickListener {
                // save state of settings
                val editor: SharedPreferences.Editor = settingPrefs.edit()
                level.locked = false
                editor.putInt("stars", totalStars - cost)
                editor.putString(String.format("level%d", level.id), gson.toJson(level))
                editor.apply()
                callback.dialogCallback(true)
                builder.dismiss()
            }
            purchaseText.text = String.format("Unlock Level %d for %d Star?", levelId, cost)
        } else {
            unlockButton.visibility = View.INVISIBLE
            purchaseText.text = String.format("Not enough Stars. Costs %d Star.", cost)
        }
        val cancelButton = view.findViewById<Button>(R.id.cancelPurchase)
        cancelButton.setOnClickListener {
            //              if the cancel button is hit, don't save settings and exit
            builder.cancel()
            callback.dialogCallback(false)
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}