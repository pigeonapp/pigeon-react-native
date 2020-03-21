@file:JvmName("RatingNotification")
package io.pigeonapp.notificationbuilders

import io.pigeonapp.R
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.facebook.react.bridge.WritableMap
import io.pigeonapp.PigeonClient
import java.util.concurrent.atomic.AtomicInteger

fun showRating(message: WritableMap) {
    val channelId = PigeonNotificationChannel.id
    val channelName = PigeonNotificationChannel.name
    val id = AtomicInteger(0)
    val notifyId = id.incrementAndGet()

    val applicationContext = PigeonClient.getInstance().reactApplicationContext.applicationContext as Application
    val currentAppIcon = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA).icon

    val collapsedView = RemoteViews(applicationContext.packageName, R.layout.push_collapsed)
    collapsedView.setTextViewText(R.id.notificationTitle, "Sample")
    collapsedView.setTextViewText(R.id.notificationTitle, "Sample")

    val ratingView = RemoteViews(applicationContext.packageName, R.layout.push_rating)
    ratingView.setTextViewText(R.id.notificationTitle, "Sample")
    ratingView.setTextViewText(R.id.notificationText, "Sample")

    val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(currentAppIcon)
        .setCustomContentView(collapsedView)
        .setCustomBigContentView(ratingView)

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
