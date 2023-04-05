package com.example.csci4176_groupproject

data class CosmeticList(
    val itemList: MutableList<Cosmetic> = mutableListOf(
    Cosmetic("Default", "", R.drawable.ic_launcher_foreground, 0),
    Cosmetic("Red Ball", "Normal Skin (Cost:3)", R.drawable.locked_redball, 3),
    Cosmetic("Blue Ball", "Normal Skin (Cost:3)", R.drawable.locked_blueball, 3),
    Cosmetic("Devil Ball", "Unique Skin (Cost:7)", R.drawable.locked_devilball, 7),
    Cosmetic("Sun Ball", "Unique Skin (Cost:7)", R.drawable.locked_sunball, 7),
    ),

    val skinList: List<Int> = listOf(
        R.drawable.ic_launcher_foreground,
        R.drawable.redball,
        R.drawable.blueball,
        R.drawable.devil,
        R.drawable.sun
    )
)
