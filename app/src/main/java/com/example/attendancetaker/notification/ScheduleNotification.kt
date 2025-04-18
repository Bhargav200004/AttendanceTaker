package com.example.attendancetaker.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.attendancetaker.AlarmReceiver


// Set the notification
fun scheduleNotification(context : Context , timeInMillis: Long){

    // Get the intent
    val intent = Intent(context , AlarmReceiver::class.java)

    // Get the pending intent
    val pendingIntent = PendingIntent.getBroadcast(
        context, // Context
        0, // Request code
        intent, // Intent
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT // Flags
    )

    // Get the alarm manager
    val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Set the notification
    alarmManger.setExact(
        AlarmManager.RTC_WAKEUP, // Type
        timeInMillis, // Time
        pendingIntent // Pending intent
    )
}