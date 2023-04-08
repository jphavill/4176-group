//Contributors: Justin MacKinnon, Jason Havill
package com.example.csci4176_groupproject.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class TimeCalcs {
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeDifference(startTime: LocalDateTime, endTime: LocalDateTime): Int {
        // convert time that has passed to millseconds
        var ms = 0

        if (endTime.dayOfYear != startTime.dayOfYear) {
            ms += 1000 * 60 * 60 * 24 * (endTime.dayOfYear - startTime.dayOfYear)
        }
        if (endTime.hour != startTime.hour) {
            ms += 1000 * 60 * 60 * (endTime.hour - startTime.hour)
        }
        if (endTime.minute != startTime.minute) {
            ms += 1000 * 60 * (endTime.minute - startTime.minute)
        }
        if (endTime.second != startTime.second) {
            ms += 1000 * (endTime.second - startTime.second)
        }
        if (endTime.nano != startTime.nano) {
            ms += ((endTime.nano - startTime.nano) * 0.000001).toInt()
        }

        return ms
    }
}