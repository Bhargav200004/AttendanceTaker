package com.example.attendancetaker

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.attendancetaker.notification.HolidayList.holidays
import com.example.attendancetaker.notification.scheduleNextValidDay
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver()  {

    override fun onReceive(context: Context, intent: Intent?) {

        val notificationManager = context.getSystemService(NotificationManager::class.java)

        val notification = NotificationCompat.Builder(context, BuildConfig.ATTENDANCE_NOTIFICATION_CHANNEL)
            .setContentTitle("Attendance Reminder")
            .setContentText("Time to take attendance")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )

        scheduleNextValidDay(context, holidays)
    }
}