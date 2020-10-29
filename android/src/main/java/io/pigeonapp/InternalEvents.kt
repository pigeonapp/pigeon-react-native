@file:JvmName("InternalEvents")
package io.pigeonapp

import android.content.SharedPreferences
import android.os.Build
import android.text.format.DateUtils
import com.facebook.react.bridge.Arguments
import kotlin.math.roundToInt

fun PigeonClient.trackAppStarted() {
    val context = this.reactApplicationContext.applicationContext
    val packageInfo = context.getPackageInfo()
    val currentBuild = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }

    val eventProperties = Arguments.createMap()
    eventProperties.putString("version", packageInfo.versionName)
    eventProperties.putString("build", currentBuild.toString())

    when {
        context.getPigeonSharedPreferences().getLong(SHARED_PREFERENCES_KEY_BUILD, Long.MIN_VALUE) == Long.MIN_VALUE ->
            this.track(INTERNAL_EVENT_APP_INSTALLED, eventProperties)
        context.getPigeonSharedPreferences().getLong(SHARED_PREFERENCES_KEY_BUILD, Long.MIN_VALUE) < currentBuild ->
            this.track(INTERNAL_EVENT_APP_UPDATED, eventProperties)
        else -> this.track(INTERNAL_EVENT_APP_OPENED, eventProperties)
    }

    val editor: SharedPreferences.Editor = context.getPigeonSharedPreferences().edit()
    editor.putString(SHARED_PREFERENCES_KEY_VERSION, packageInfo.versionName)
    editor.putLong(SHARED_PREFERENCES_KEY_BUILD, currentBuild)
    editor.apply()
}

fun PigeonClient.trackAppSession(elapsedTime: Double) {
    val elapsedTimeRounded = ((elapsedTime / 1000) * 10.0).roundToInt() / 10.0
    val eventProperties = Arguments.createMap()
    eventProperties.putString("session_length", DateUtils.formatElapsedTime(elapsedTimeRounded.toLong()))
    this.track(INTERNAL_EVENT_APP_BACKGROUNDED, eventProperties)
}

fun PigeonClient.trackAppCrashed(exception: Throwable?) {
    val message = exception?.message
    val eventProperties = Arguments.createMap()
    eventProperties.putString("exception", message)
    this.track(INTERNAL_EVENT_APP_CRASHED, eventProperties)
}
