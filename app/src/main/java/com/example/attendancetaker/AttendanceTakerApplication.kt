package com.example.attendancetaker

import android.app.Application
import android.util.Log
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
