package com.passportv3locity.notificationmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.passportv3locity.R

class NotificationHelper @RequiresApi(api = Build.VERSION_CODES.O) constructor(base: Context?) :
    ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createChannels() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager!!.createNotificationChannel(notificationChannel)
    }

    fun getNotification(
        title: String?, body: String?, resultPendingIntent: PendingIntent?, bitmap: Bitmap
    ): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Notification.Builder(applicationContext, CHANNEL_ONE_ID).setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher).setContentText(body)
                .setContentIntent(resultPendingIntent).setStyle(
                    Notification.BigPictureStyle().bigPicture(bitmap).setContentDescription(body)
                ).setPriority(Notification.PRIORITY_MIN)
                .setVibrate(longArrayOf(100L, 100L, 200L, 500L)).setAutoCancel(true)
        } else {
            Notification.Builder(applicationContext, CHANNEL_ONE_ID).setContentTitle(title)
                .setContentText(body).setContentIntent(resultPendingIntent).setStyle(
                    Notification.BigPictureStyle().bigPicture(bitmap).setSummaryText(body)
                ).setSmallIcon(R.mipmap.ic_launcher).setTicker(body)
                .setPriority(Notification.PRIORITY_MIN)
                .setVibrate(longArrayOf(100L, 100L, 200L, 500L)).setAutoCancel(true)
        }
    }

    fun getNotificationTwo(
        title: String?, body: String?, resultPendingIntent: PendingIntent?
    ): Notification.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Notification.Builder(applicationContext, CHANNEL_ONE_ID).setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher).setContentText(body)
                .setContentIntent(resultPendingIntent).setStyle(
                    Notification.BigTextStyle().bigText(body)
                ).setPriority(Notification.PRIORITY_MIN)
                .setVibrate(longArrayOf(100L, 100L, 200L, 500L)).setAutoCancel(true)
        } else {
            Notification.Builder(applicationContext, CHANNEL_ONE_ID).setContentTitle(title)
                .setContentText(body).setContentIntent(resultPendingIntent)
                .setStyle(Notification.BigTextStyle().bigText(body))
                .setSmallIcon(R.mipmap.ic_launcher).setTicker(body)
                .setPriority(Notification.PRIORITY_MIN)
                .setVibrate(longArrayOf(100L, 100L, 200L, 500L)).setAutoCancel(true)
        }
    }

    fun notify(id: Int, notification: Notification.Builder) {
        manager!!.notify(id, notification.build())
    }

    private val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    companion object {
        const val CHANNEL_ONE_ID = "com.passportv3locity.notificationmanager"
        const val CHANNEL_ONE_NAME = "Passportv3locity"
    }

    init {
        createChannels()
    }
}