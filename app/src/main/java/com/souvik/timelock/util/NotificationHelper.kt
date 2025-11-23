package com.souvik.timelock.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.souvik.timelock.R

object NotificationHelper {

    private const val CHANNEL_ID_TIMEUP = "timeup_channel"
    private const val CHANNEL_ID_5MIN = "timeup_5min_channel"

    private const val NOTIF_ID_TIMEUP = 1002
    private const val NOTIF_ID_5MIN = 1003

    // Call this ONCE (e.g., in Application class)
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel1 = NotificationChannel(
                CHANNEL_ID_TIMEUP,
                "Time Up Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )

            val channel2 = NotificationChannel(
                CHANNEL_ID_5MIN,
                "Remaining Time Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }
    }

    // ------------ 5 MINUTES LEFT NOTIFICATION ------------
    fun sendFiveMinutesLeft(context: Context, appName: String) {

        // Android 13+ Permission check
        if (Build.VERSION.SDK_INT >= 33) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val granted = context.checkSelfPermission(permission)
            if (granted != android.content.pm.PackageManager.PERMISSION_GRANTED) return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_5MIN)
            .setSmallIcon(R.drawable.ic_placeholder)   // FIXED
            .setContentTitle("5 Minutes Remaining")
            .setContentText("You have 5 minutes left on $appName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(NOTIF_ID_5MIN, builder.build())
    }

    // ------------ TIME'S UP NOTIFICATION ------------
    fun sendTimeUpNotification(context: Context, appName: String) {

        if (Build.VERSION.SDK_INT >= 33) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val granted = context.checkSelfPermission(permission)
            if (granted != android.content.pm.PackageManager.PERMISSION_GRANTED) return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_TIMEUP)
            .setSmallIcon(R.drawable.ic_placeholder)
            .setContentTitle("Time Limit Reached")
            .setContentText("$appName has reached its limit.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(NOTIF_ID_TIMEUP, builder.build())
    }
}
