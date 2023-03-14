package com.example.csci4176_groupproject

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Switch
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog

class settingsDialog(context: Context) : AlertDialog.Builder(context)  {

    fun showSettings() {
    //        get a reference to the fragments viewbinding to set the picture and text
        Log.d("settings", "making settings view:")
        val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)

        val builder = AlertDialog.Builder(context, R.style.SettingsDialog)
            .create()

        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.settings_dialog, null)
        builder.setView(view)
        // set the state of the settings
        val colorBlindModeView = view.findViewById<Switch>(R.id.colorBlindSwitch)
        colorBlindModeView.isChecked = settingPrefs.getBoolean("colorBlind", false)
        val hapticsSwitchView = view.findViewById<Switch>(R.id.hapticsSwitch)
        hapticsSwitchView.isChecked = settingPrefs.getBoolean("haptics", true)
        val soundView = view.findViewById<ToggleButton>(R.id.soundToggle)
        soundView.isChecked = settingPrefs.getBoolean("sound", false)

        val applyButton = view.findViewById<Button>(R.id.applyButton)
        applyButton.setOnClickListener {

            // save state of settings
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            editor.putBoolean("colorBlind", colorBlindModeView.isChecked)
            editor.putBoolean("haptics", hapticsSwitchView.isChecked)
            editor.putBoolean("sound", soundView.isChecked)
            editor.apply()

            builder.dismiss()
        }
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
    //              if the cancel button is hit, don't save settings and exit
            builder.cancel()
        }
    //          the user must hit either the cancel or apply button to close the dialog
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}