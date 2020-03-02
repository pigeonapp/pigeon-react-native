@file:JvmName("SimpleNotification")

package io.pigeonapp.notificationbuilders

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import io.pigeonapp.PigeonClient
import java.util.concurrent.atomic.AtomicInteger

fun show(notification: RemoteMessage.Notification) {
    val channelId = PigeonNotificationChannel.id
    val channelName = PigeonNotificationChannel.name
    val id = AtomicInteger(0)
    val notifyId = id.incrementAndGet()

    val applicationContext = PigeonClient.getInstance().reactApplicationContext.applicationContext as Application
    val currentAppIcon = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA).icon

    var notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(currentAppIcon)
        .setContentTitle(notification.title)
        .setContentText(notification.body)

    if (notification.imageUrl != null) {
        val image = Picasso.get().load(notification.imageUrl).get()

        notificationBuilder = notificationBuilder.setLargeIcon(image)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture(image)
                .setBigContentTitle(notification.title)
                .setSummaryText(notification.body)
            )
    }

    val notificationManager: NotificationManager? = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.setShowBadge(false)

        notificationManager?.apply {
            createNotificationChannel(channel)
            notify(notifyId, notificationBuilder.build())
        }
    } else {
        notificationManager?.notify(notifyId, notificationBuilder.build())
    }
}

