package com.example.attendancetaker.notification

import android.content.Context
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

// Schedule the next valid day function
fun scheduleNextValidDay(context: Context , holidays: List<String>){
    // Get the next valid day
    var nextDate = LocalDate.now().plusDays(1)

    // Skipping weekends and holidays
    while (!isWorkingDay(nextDate, holidays)){
        nextDate = nextDate.plusDays(1)
    }

    val time = LocalTime.of(7,30,0) // 7:30 AM

    // Setting up next day and time to notification
    val dateTime = LocalDateTime.of(nextDate , time)


    // Convert date and time in milli second
    val millis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    // Schedule the notification
    scheduleNotification(context = context , millis)
}