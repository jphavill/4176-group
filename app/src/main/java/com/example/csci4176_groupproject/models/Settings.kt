//Contributors: Jason Havill
package com.example.csci4176_groupproject.models

import com.example.csci4176_groupproject.data.CosmeticList

data class Settings(
    // default settings
    var haptics: Boolean = true,
    var colourBlindMode: Boolean = false,
    val playerSkin: Int = CosmeticList().itemList[0].img,
    val resetLevels: Boolean = false,
    val resetStore: Boolean = false,
    // tracks what settings have been updated
    var changes: Map<SettingChange, Boolean> = mapOf(
        SettingChange.Haptics to false,
        SettingChange.ColourBlindMode to false,
        SettingChange.PlayerSkin to false,
        SettingChange.ResetLevels to false,
        SettingChange.ResetStore to false,
    )
)

enum class SettingChange {
    Haptics, ColourBlindMode, PlayerSkin, ResetLevels, ResetStore
}