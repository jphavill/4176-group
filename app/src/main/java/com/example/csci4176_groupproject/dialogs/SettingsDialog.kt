package com.example.csci4176_groupproject.dialogs

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.avoidDropdownFocus
import com.example.csci4176_groupproject.data.CosmeticList
import com.example.csci4176_groupproject.interfaces.SettingsDialogCallback
import com.example.csci4176_groupproject.models.Cosmetic
import com.example.csci4176_groupproject.models.SettingChange
import com.example.csci4176_groupproject.models.Settings
import com.google.gson.Gson

class SettingsDialog(context: Context) : AlertDialog.Builder(context) {
    private val settingPrefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("settingsPrefs", 0)

    fun showSettings(callback: SettingsDialogCallback) {
        //        get a reference to the fragments viewbinding to set the picture and text
        val builder = AlertDialog.Builder(context, R.style.SettingsDialog)
            .create()

        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.settings_dialog, null)
        builder.setView(view)

        val initialSettings = Settings(
            haptics = settingPrefs.getBoolean("haptics", true),
            colourBlindMode = settingPrefs.getBoolean("colorBlind", false),
            playerSkin = settingPrefs.getInt("playerSkin", CosmeticList().itemList[0].img)
        )
        // set the state of the settings
        val colorBlindModeView = view.findViewById<SwitchCompat>(R.id.colorBlindSwitch)
        colorBlindModeView.isChecked = initialSettings.colourBlindMode
        val hapticsSwitchView = view.findViewById<SwitchCompat>(R.id.hapticsSwitch)
        hapticsSwitchView.isChecked = initialSettings.haptics

        // used for development testing
        val resetLevelToggle = view.findViewById<ToggleButton>(R.id.resetLevelsButton)

        val resetStoreToggle = view.findViewById<ToggleButton>(R.id.resetStoreButton)


        val skinNames = mutableListOf<String>()
        val skinIcons = mutableListOf<Int>()

        for (i in 0 until CosmeticList().itemList.size) {
            val gson = Gson()
            val cosmetic = gson.fromJson(
                settingPrefs.getString(
                    String.format("cosmetic%d", i), gson.toJson(
                        CosmeticList().itemList[i]
                    )
                ), Cosmetic::class.java
            )
            // only add the other icons that are also unlocked (purchased)
            // default player icon is always in the list
            if (!cosmetic.locked || cosmetic.title == "Default") {
                skinNames.add(cosmetic.title)
                skinIcons.add(cosmetic.img)
            }
        }

        val playerIconView = view.findViewById<Spinner>(R.id.playerIconSelect)
        ArrayAdapter(
            context,
            R.layout.skin_list,
            skinNames
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.skin_list)
            playerIconView.adapter = adapter
        }
        playerIconView.avoidDropdownFocus()

        playerIconView.setSelection(
            skinIcons.indexOf(
                settingPrefs.getInt(
                    "playerSkin",
                    CosmeticList().itemList[0].img
                )
            )
        )

        val applyButton = view.findViewById<Button>(R.id.applyButton)
        applyButton.setOnClickListener {
            val updatedSettings = Settings(
                haptics = hapticsSwitchView.isChecked,
                colourBlindMode = colorBlindModeView.isChecked,
                playerSkin = skinIcons[playerIconView.selectedItemPosition]
            )
            updatedSettings.changes = mapOf(
                SettingChange.Haptics to (initialSettings.haptics != updatedSettings.haptics),
                SettingChange.ColourBlindMode to (initialSettings.colourBlindMode != updatedSettings.colourBlindMode),
                SettingChange.PlayerSkin to (initialSettings.playerSkin != updatedSettings.playerSkin),
                SettingChange.ResetLevels to resetLevelToggle.isChecked,
                SettingChange.ResetStore to resetStoreToggle.isChecked,
            )

            // save state of settings
            val editor: SharedPreferences.Editor = settingPrefs.edit()
            editor.putBoolean("colorBlind", updatedSettings.colourBlindMode)
            editor.putBoolean("haptics", updatedSettings.haptics)
            editor.putInt("playerSkin", updatedSettings.playerSkin)
            editor.apply()
            callback.settingsDialogCallback(updatedSettings)

            builder.dismiss()
        }


        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            //  if the cancel button is hit, don't save settings and exit
            builder.cancel()
        }
        //  the user must hit either the cancel or apply button to close the dialog
        builder.setCanceledOnTouchOutside(false)
//      keeps the android status bar from appearing when the dialog is open
        builder.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        builder.show()
    }
}