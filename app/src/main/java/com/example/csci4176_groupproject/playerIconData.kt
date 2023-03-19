package com.example.csci4176_groupproject

val PLAYER_ICONS = listOf<playerIconData>(
    playerIconData(R.drawable.ic_launcher_foreground, "Default"),
    playerIconData(android.R.drawable.star_on, "Star"),
    playerIconData(android.R.drawable.ic_menu_myplaces, "Circle")
)

val PLAYER_ICONS_MAP = mapOf(
    "Default" to 0,
    "Star" to 1,
    "Circle" to 2

)
data class playerIconData(
    val iconResource: Int,
    val name: String
)
