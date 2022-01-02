package com.example.android.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

@BindingAdapter("sleepQuality")
fun TextView.setSleepQuality(item: SleepNight?) {
    item?.apply {
        val res = context.resources
        text = convertNumericQualityToString(item.sleepQuality, res)
    }
}

@BindingAdapter("sleepDuration")
fun TextView.setSleepDuration(item: SleepNight?) {
    item?.apply {
        val res = context.resources
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
    }
}

@BindingAdapter("sleepIcon")
fun ImageView.setSleepIcon(item: SleepNight?) {
    item?.apply {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_launcher_sleep_tracker_foreground
        })
    }
}
