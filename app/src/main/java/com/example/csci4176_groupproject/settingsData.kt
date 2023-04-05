package com.example.csci4176_groupproject

data class settingsData(
    var haptics: Boolean = true,
    var colourBlindMode: Boolean = false,
    val playerSkin: Int = CosmeticList().itemList[0].img,
    // tracks what settings have been updated
    var changes: Map<String, Boolean> = mapOf(
        "haptics" to false,
        "colourBlindMode" to false,
        "playerSkin" to false,
        )
    )
