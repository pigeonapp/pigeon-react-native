@file:JvmName("CarousalNotification")
package io.pigeonapp.notificationbuilders

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import io.pigeonapp.PigeonClient
import io.pigeonapp.R
import java.util.concurrent.atomic.AtomicInteger

fun showCarousal(notification: RemoteMessage.Notification) {
    val channelId = PigeonNotificationChannel.id
    val channelName = PigeonNotificationChannel.name
    val id = AtomicInteger(0)
    val notifyId = id.incrementAndGet()
    val imgUrls = listOf("https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1650&q=80", "https://images.unsplash.com/photo-1444212477490-ca407925329e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1700&q=80", "https://images.unsplash.com/photo-1541876176131-3f5e84a7331a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1651&q=80")


    val applicationContext = PigeonClient.getInstance().reactApplicationContext.applicationContext as Application
    val packageName = applicationContext.packageName
    val currentAppIcon = applicationContext.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).icon


    val expandedView = RemoteViews(packageName, R.layout.notification_slider_layout)
    expandedView.setTextViewText(R.id.textViewTitle, notification.title)


    imgUrls.forEach {
        val image = Picasso.get().load(it).get()
        val viewFlipperImage = RemoteViews(packageName, R.layout.notification_slider_layout)
        viewFlipperImage.setImageViewBitmap(R.id.imageView, image)
        expandedView.addView(R.id.viewFlipper, viewFlipperImage)
    }

    val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(currentAppIcon)
        .setContentTitle(notification.title)
        .setContentText(notification.body)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomBigContentView(expandedView)

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

