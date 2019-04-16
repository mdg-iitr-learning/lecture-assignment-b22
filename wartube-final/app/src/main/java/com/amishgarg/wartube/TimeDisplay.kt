package com.amishgarg.wartube

import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.util.*

class TimeDisplay(timestamp: Long) {

    val timestamp: Long = timestamp
    val current : Long = System.currentTimeMillis()
    val distanceMillis : Long = current-timestamp


    val seconds = (distanceMillis / 1000).toDouble()
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val years = days / 365

    var time : String = ""

    fun getDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(Date(timestamp))
    }

    fun getTimeDisplay() : String
    {
        if(days > 4)
        {
            return getDate()
        }
        else {
            if(seconds < 10)
            {
                time = "just now"
            }
            else if (seconds < 45)
                time = "${Math.round(seconds)}s"
            else if (seconds < 90)
                time = "1m"
            else if (minutes < 45)
                time = "${Math.round(minutes)}m"
            else if (minutes < 90)
                time = "1h"
            else if (hours < 24)
                time = "${Math.round(hours)}h"
            else if (hours < 48)
                time = "1d"
            else if (days < 4)
                time = "${Math.round(days)}d"
        }
        return time
    }

}