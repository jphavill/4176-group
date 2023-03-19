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
    private val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)
    private val initalColourBlindMode = settingPrefs.getBoolean("colorBlind", false)
    fun showSettings(callback: dialogCallback) {
    //        get a reference to the fragments viewbinding to set the picture and text
        val builder = AlertDialog.Builder(context, R.style.SettingsDialog)
            .create()

        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.settings_dialog, null)
        builder.setView(view)
        // set the state of the settings
        val colorBlindModeView = view.findViewById<Switch>(R.id.colorBlindSwitch)
        colorBlindModeView.isChecked = initalColourBlindMode
        val hapticsSwitchView = view.findViewById<Switch>(R.id.hapticsSwitch)
        hapticsSwitchView.isChecked = settingPrefs.getBoolean("haptics", true)
        val soundView = view.findViewById<ToggleButton>(R.id.soundToggle)
        soundView.isChecked = settingPrefs.getBoolean("sound", false)

        val applyButton = view.findViewById<Button>(R.id.mainMenuButton)
        applyButton.setOnClickListener {

            // save state of settings
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            editor.putBoolean("colorBlind", colorBlindModeView.isChecked)
            if (initalColourBlindMode != colorBlindModeView.isChecked) {
                callback.dialogCallback(colorBlindModeView.isChecked)
            }
            editor.putBoolean("haptics", hapticsSwitchView.isChecked)
            editor.putBoolean("sound", soundView.isChecked)
            editor.apply()

            builder.dismiss()
        }
        val cancelButton = view.findViewById<Button>(R.id.nextLevelButton)
        cancelButton.setOnClickListener {
    //              if the cancel button is hit, don't save settings and exit
            builder.cancel()
        }
    //          the user must hit either the cancel or apply button to close the dialog
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}