package com.bignerdranch.android.nudge.habittracker.logic.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Calculations {

    // Calculates the time between the dates
    fun calculateTimeBetweenDates(startDate: String): String {

        val endDate = timeStampToString(System.currentTimeMillis())

        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm")
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)

        var isNegative = false

        var difference = end.time - start.time
        if (difference < 0) {
            difference = -(difference)
            isNegative = true
        }

        val minutes = difference / 60 / 1000
        val hours = difference / 60 / 1000 / 60
        val days = (difference / 60 / 1000 / 60) / 24
        val months = (difference / 60 / 1000 / 60) / 24 / (365 / 12)
        val years = difference / 60 / 1000 / 60 / 24 / 365

        if (isNegative) {

            return when {
                minutes < 240 -> "Starts in $minutes minutes"
                hours < 48 -> "Starts in $hours hours"
                days < 61 -> "Starts in $days days"
                months < 24 -> "Starts in $months months"
                else -> "Starts in $years years"
            }
        }

        return when {
            minutes < 240 -> "$minutes minutes ago"
            hours < 48 -> "$hours hours ago"
            days < 61 -> "$days days ago"
            months < 24 -> "$months months ago"
            else -> "$years years ago"
        }
    }

    private fun timeStampToString(timeStamp: Long): String {

        val stamp = Timestamp(timeStamp)
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm")
        val date = sdf.format(Date(stamp.time))

        return date.toString()
    }

    fun formatDate(_day: Int, _month: Int, _year: Int): String {
        var day = _day.toString()
        var month = _month.toString()

        if (_day < 10) {
            day = "0$_day"
        }

        if (_month < 9) { //Because the month instance we retrieve starts at 0 and it's stupid!
            month = "0${_month + 1}"
        } else if (_month >= 9 && _month <= 11) {
            month = (_month + 1).toString()
        }

        return "$month/$day/$_year"
    }

    fun formatTime(_hour: Int, _minute: Int): String {
        var hour = _hour.toString()
        var minute = _minute.toString()

        if (_hour < 10) {
            hour = "0$_hour"
        }
        if (_minute < 10) {
            minute = "0$_minute"
        }
        return "$hour:$minute"
    }
}