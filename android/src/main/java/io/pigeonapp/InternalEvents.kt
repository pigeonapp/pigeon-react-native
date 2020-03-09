@file:JvmName("InternalEvents")
package io.pigeonapp

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.facebook.react.bridge.Arguments
import kotlin.math.roundToInt

fun Context.getPackageInfo(): PackageInfo = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_META_DATA)

fun getPrefKey(context: Context) = "$PACKAGE_NAME.$DEFAULT_INSTANCE.$context.packageName"

fun Context.getPigeonSharedPreferences(): SharedPreferences = this.getSharedPreferences(getPrefKey(this), Context.MODE_PRIVATE)

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
        context.getPigeonSharedPreferences().getLong(PREF_KEY_BUILD, Long.MIN_VALUE) == Long.MIN_VALUE ->
            this.track(INTERNAL_EVENT_FIRST_OPEN, eventProperties)
        context.getPigeonSharedPreferences().getLong(PREF_KEY_BUILD, Long.MIN_VALUE) < currentBuild ->
            this.track(INTERNAL_EVENT_APP_UPDATED, eventProperties)
        else -> this.track(INTERNAL_EVENT_APP_OPENED, eventProperties)
    }

    val editor: SharedPreferences.Editor = context.getPigeonSharedPreferences().edit()
    editor.putString(PREF_KEY_VERSION_NAME, packageInfo.versionName)
    editor.putLong(PREF_KEY_BUILD, currentBuild)
    editor.apply()
}

fun PigeonClient.trackAppSession(elapsedTime: Double) {
    val elapsedTimeRounded = ((elapsedTime / 1000) * 10.0).roundToInt() / 10.0
    val eventProperties = Arguments.createMap()
    eventProperties.putString("session_length", elapsedTimeRounded.toString())
    this.track(INTERNAL_EVENT_SESSION, eventProperties)
}
