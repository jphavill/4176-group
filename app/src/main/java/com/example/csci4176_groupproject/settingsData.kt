package com.example.csci4176_groupproject

data class settingsData(
    var haptics: Boolean = true,
    var colourBlindMode: Boolean = false,
    val playerIcon: String = "Default",
    var levelTheme: String = "Default",
    var sound: Boolean = false,
    // tracks what settings have been updated
    var changes: Map<String, Boolean> = mapOf(
        "haptics" to false,
        "colourBlindMode" to false,
        "playerIcon" to false,
        "levelTheme" to false,
        "sound" to false
        )
    )
