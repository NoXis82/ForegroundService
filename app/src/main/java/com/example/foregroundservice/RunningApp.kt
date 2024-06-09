package com.example.foregroundservice

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class RunningApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "running_channel",
            "Running notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}