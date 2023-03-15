package com.example.csci4176_groupproject

import androidx.appcompat.app.AppCompatActivity

data class levelActivities(
    val levels: List<Class<out AppCompatActivity>> = listOf(Level1Activity::class.java,
        Level2Activity::class.java,
        Level3Activity::class.java,
        Level4Activity::class.java,
        Level5Activity::class.java,
        Level6Activity::class.java,
        Level7Activity::class.java,
        Level8Activity::class.java,
        Level9Activity::class.java,
        Level10Activity::class.java,
    ),
    val numLevels: Int = 10

)
