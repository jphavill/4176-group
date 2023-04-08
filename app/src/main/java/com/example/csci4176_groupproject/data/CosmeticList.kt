//Contributors: Jason Havill, Dongkyu Kim
package com.example.csci4176_groupproject.data

import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.models.Cosmetic

data class CosmeticList(
    // stores the list of cosmetics a user can possibly unlock, including the default skin which is
    // always unlocked
    val itemList: MutableList<Cosmetic> = mutableListOf(
        Cosmetic("Default", "", R.drawable.ic_launcher_foreground, 0),
        Cosmetic("Red Ball", "Normal Skin (Cost:3)", R.drawable.locked_redball, 3),
        Cosmetic("Blue Ball", "Normal Skin (Cost:3)", R.drawable.locked_blueball, 3),
        Cosmetic("Devil Ball", "Unique Skin (Cost:7)", R.drawable.locked_devilball, 7),
        Cosmetic("Sun Ball", "Unique Skin (Cost:7)", R.drawable.locked_sunball, 7),
    ),

    // stores the unlocked versions of the player skins
    val skinList: List<Int> = listOf(
        R.drawable.ic_launcher_foreground,
        R.drawable.redball,
        R.drawable.blueball,
        R.drawable.devilball,
        R.drawable.sunball
    )
)
