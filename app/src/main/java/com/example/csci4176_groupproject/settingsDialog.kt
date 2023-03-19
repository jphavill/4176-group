package com.example.csci4176_groupproject

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog

class settingsDialog(context: Context) : AlertDialog.Builder(context)  {
    private val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)
    fun showSettings(callback: settingsDialogCallback) {
    //        get a reference to the fragments viewbinding to set the picture and text
        val builder = AlertDialog.Builder(context, R.style.SettingsDialog)
            .create()

        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.settings_dialog, null)
        builder.setView(view)

        val initialSettings = settingsData(haptics = settingPrefs.getBoolean("haptics", true),
            colourBlindMode = settingPrefs.getBoolean("colorBlind", false),
            sound = settingPrefs.getBoolean("sound", false))
        // set the state of the settings
        val colorBlindModeView = view.findViewById<Switch>(R.id.colorBlindSwitch)
        colorBlindModeView.isChecked = initialSettings.colourBlindMode
        val hapticsSwitchView = view.findViewById<Switch>(R.id.hapticsSwitch)
        hapticsSwitchView.isChecked = initialSettings.haptics
        val soundView = view.findViewById<ToggleButton>(R.id.soundToggle)
        soundView.isChecked = initialSettings.sound

        val playerIcons: List<String> =  listOf("icon 1", "icon 2")

        val playerIconView = view.findViewById<Spinner>(R.id.playerIconSelect)
        ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            playerIcons
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            playerIconView.adapter = adapter
        }

        val applyButton = view.findViewById<Button>(R.id.mainMenuButton)
        applyButton.setOnClickListener {
            val updatedSettings = settingsData(haptics = hapticsSwitchView.isChecked,
                colourBlindMode = colorBlindModeView.isChecked,
                sound = soundView.isChecked)
            updatedSettings.changes = mapOf(
                "haptics" to (initialSettings.haptics != updatedSettings.haptics),
                "colourBlindMode" to (initialSettings.colourBlindMode != updatedSettings.colourBlindMode),
                "playerIcon" to (initialSettings.playerIcon != updatedSettings.playerIcon),
                "levelTheme" to (initialSettings.levelTheme != updatedSettings.levelTheme),
                "sound" to (initialSettings.sound != updatedSettings.sound)
            )
            callback.settingsDialogCallback(updatedSettings)
            // save state of settings
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            editor.putBoolean("colorBlind", colorBlindModeView.isChecked)
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