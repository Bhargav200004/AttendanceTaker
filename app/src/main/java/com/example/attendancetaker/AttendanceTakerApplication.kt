package com.example.attendancetaker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.media.AudioAttributes
import androidx.core.net.toUri
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AttendanceTakerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //Notification Channel for (8++ version )
        val soundUri = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/raw/notification_sound".toUri()

        val notificationChannel = NotificationChannel(
            BuildConfig.ATTENDANCE_NOTIFICATION_CHANNEL,
            BuildConfig.ATTENDANCE_NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(
                soundUri,
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)


        // Custom Format Strategy for Logger
        val formatStrategy: FormatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(true)   // Displays thread info for better traceability
            .methodCount(2)         // Shows 2 methods in the stack trace for clarity
            .methodOffset(5)        // Ensures accurate method display in logs
            .tag("") // Custom global tag for easier identification
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Logger.log(priority, "-$tag", message, t)
                }

                override fun createStackElementTag(element: StackTraceElement): String {
                    return "${element.fileName}:${element.lineNumber}#${element.methodName}()"
                }
            })
        }
    }
}
