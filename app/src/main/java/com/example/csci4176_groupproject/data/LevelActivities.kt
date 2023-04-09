//Contributors: Jason Havill
package com.example.csci4176_groupproject.data

import androidx.appcompat.app.AppCompatActivity
import com.example.csci4176_groupproject.activities.levels.*

data class LevelActivities(
    // list of all the Level intents so that level select buttons can open specific levels given an index
    val levels: List<Class<out AppCompatActivity>> = listOf(
        Level1Activity::class.java,
        Level2Activity::class.java,
        Level3Activity::class.java,
        Level4Activity::class.java,
        Level5Activity::class.java,
        Level6Activity::class.java,
        Level7Activity::class.java,
        Level8Activity::class.java,
        Level9Activity::class.java,
        Level10Activity::class.java,
    )
)
