package com.passportv3locity.notificationmanager

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.passportv3locity.R.*
import com.passportv3locity.applocal.MyApplication
import com.passportv3locity.utils.AppUtil
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var notificationManager: NotificationManager? = null
    var mContext: Context? = null
    var icon = 0
    var notificationTime: Long = 0

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("TAG", "MESSAGE BODY" + remoteMessage)
        if (!AppUtil.isNullOrBlank(remoteMessage.data)) {
            Log.d("DataMessage", "onMessageReceived: " + remoteMessage.data)
            handleMessage(
                remoteMessage.data,
                remoteMessage.data.get("title").toString(),
                remoteMessage.data.get("body").toString()
            )
        }
        MyApplication.instance?.getNotificationCallbackListener()?.onNotificationReceived()
        remoteMessage.notification?.let { }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleMessage(
        data: Map<*, *>, title: String, body: String
    ) {

        var notificationType: String = data.get("notificationType").toString()
        var notificationId: String = data.get("notificationId").toString()
        var status: String = data.get("status").toString()
        var itemId: String = data.get("itemId").toString()
        var notificationUrl = data.get("url") ?: ""
        var notificationDate = data.get("createdDateTime").toString() ?: ""

        var imageBitmap: Bitmap? = null
        var url: URL? = null
        try {
            if (data.get("image") != null) {
                url = URL(data.get("image").toString())
                imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            }

        } catch (e: IOException) {
            println(e)
        }
        notificationTime = System.currentTimeMillis()
        var notificationTitle: String = title
        var notificationDescription = body
        var notificationIntent: Intent? = null
        val notification_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(
            applicationContext, notification_uri
        )

        if (title == null || title.equals("") || title.equals("null")) {
            notificationTitle = ""
        }
        if (body == null || body.equals("") || body.equals("null")) {
            notificationDescription = ""
        }

        notificationIntent?.putExtra("fromNotification", true)
        val id = (System.currentTimeMillis() * (Math.random() * 100).toInt()).toInt()

        var pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                applicationContext,
                System.currentTimeMillis().toInt(),
                notificationIntent,
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                applicationContext,
                System.currentTimeMillis().toInt(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationHelper = NotificationHelper(applicationContext)
            if (imageBitmap != null) {
                val notificationBuilder: Notification.Builder = notificationHelper.getNotification(
                    notificationTitle, notificationDescription, pendingIntent, imageBitmap!!
                )
                notificationHelper.notify(id, notificationBuilder)

            } else {
                val notificationBuilder: Notification.Builder =
                    notificationHelper.getNotificationTwo(
                        notificationTitle, notificationDescription, pendingIntent
                    )
                notificationHelper.notify(id, notificationBuilder)
            }

        } else {
            /*if (imageBitmap != null) {
                val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                    applicationContext, "com.passportv3locity.notificationmanager"
                ).setSmallIcon(drawable.app_icon).setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(imageBitmap)
                ).setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setContentTitle(notificationTitle).setContentText(notificationDescription)
                    //.addAction(drawable.mobile, "Login", pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setChannelId("com.passportv3locity.notificationmanager").setOngoing(false)
                    .setAutoCancel(true)
                if (pendingIntent != null) builder.setContentIntent(pendingIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(drawable.app_icon)
                    builder.color = resources.getColor(color.text_color_berry)
                } else {
                    builder.setSmallIcon(drawable.app_icon)
                }

                notificationManagerCompat.notify(id, builder.build())
            } else {
                val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
                val builder: NotificationCompat.Builder =
                    NotificationCompat.Builder(this, "com.passportv3locity.notificationmanager")
                        .setSmallIcon(drawable.app_icon)
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                        .setContentTitle(notificationTitle).setContentText(notificationDescription)
                        .addAction(drawable.mobile, "Login", pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setChannelId("com.passportv3locity.notificationmanager").setOngoing(false)
                        .setAutoCancel(true)
                if (pendingIntent != null) builder.setContentIntent(pendingIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(drawable.app_icon)
                    builder.color = resources.getColor(color.text_color_berry)
                } else {
                    builder.setSmallIcon(drawable.app_icon)
                }
                notificationManagerCompat.notify(id, builder.build())
            }*/
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        /*if (!AppLocalStore.mLocalStore?.firebaseToken.equals(token)) {
            SessionApp.instance?.getNewFCMTokenListener()?.onNotificationReceived()
            AppLocalStore.mLocalStore?.saveFirebaseToken(token)
        }*/
    }
}