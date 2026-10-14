package com.agriia.app.core.common

import android.content.Context
import com.example.R
import java.util.Calendar

object DateUtils {
    enum class TimeOfDay {
        MORNING, AFTERNOON, EVENING
    }

    fun getTimeOfDay(): TimeOfDay {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> TimeOfDay.MORNING
            in 12..17 -> TimeOfDay.AFTERNOON
            else -> TimeOfDay.EVENING
        }
    }

    fun getGreeting(context: Context, userName: String?): String {
        val trimmed = userName?.trim().orEmpty()
        return when (getTimeOfDay()) {
            TimeOfDay.MORNING -> if (trimmed.isNotEmpty()) context.getString(R.string.greeting_morning, trimmed) else context.getString(R.string.greeting_morning_default)
            TimeOfDay.AFTERNOON -> if (trimmed.isNotEmpty()) context.getString(R.string.greeting_afternoon, trimmed) else context.getString(R.string.greeting_afternoon_default)
            TimeOfDay.EVENING -> if (trimmed.isNotEmpty()) context.getString(R.string.greeting_evening, trimmed) else context.getString(R.string.greeting_evening_default)
        }
    }
}
