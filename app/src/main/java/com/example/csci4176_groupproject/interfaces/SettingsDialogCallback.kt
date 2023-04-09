//Contributors: Jason Havill
package com.example.csci4176_groupproject.interfaces

import com.example.csci4176_groupproject.models.Settings

interface SettingsDialogCallback {
    // used to return the updated settings to activity or fragment that launches the settings dialog
    fun settingsDialogCallback(settings: Settings)
}