package com.example.foregroundservice.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.foregroundservice.MainActivity
import com.example.foregroundservice.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RunningService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.name -> start()
            Actions.STOP.name -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("ForegroundServiceType")
    private fun start() {


        serviceScope.launch {
            (1..4).forEach {
                val intent = Intent(this@RunningService, MainActivity::class.java).apply {
                    flags = //Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                intent.putExtra("id", "Test id: $it")
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                    this@RunningService,
                    it,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )



                val notification =
                    NotificationCompat.Builder(this@RunningService, "running_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Run is active")
                        .setContentText("Elapsed time: 0${it}:50")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build()
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    startForeground(
//                        it,
//                        notification,
//                        FOREGROUND_SERVICE_TYPE_LOCATION
//                    )
//                } else {
                startForeground(it, notification)
//                }
                delay(1000)
            }
        }


    }


    enum class Actions {
        START,
        STOP
    }
}

