package io.pigeonapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import com.facebook.react.bridge.Arguments


class PigeonActivityLifecycleCallbacks private constructor() : Application.ActivityLifecycleCallbacks {
    private val TAG = PigeonActivityLifecycleCallbacks::class.java.simpleName
    private val pigeonClient: PigeonClient = PigeonClient.getInstance()
    private var startSession: Double = System.currentTimeMillis().toDouble()

    companion object {
        private var pigeonActivityLifecycleCallbacks: PigeonActivityLifecycleCallbacks? = null
        private var shouldRecordScreenViews = false

        fun enablePigeonActivityLifecycleCallbacks(context: Context) {
            if (pigeonActivityLifecycleCallbacks != null) {
                return
            }

            pigeonActivityLifecycleCallbacks = PigeonActivityLifecycleCallbacks()
            val application = context as Application
            application.registerActivityLifecycleCallbacks(pigeonActivityLifecycleCallbacks)
        }

        fun enableRecordScreenViews() {
            shouldRecordScreenViews = true
        }
    }

    init {
        pigeonClient.trackAppStarted()
    }

    override fun onActivityPaused(activity: Activity?) {
        pigeonClient.trackAppSession(System.currentTimeMillis().toDouble() - startSession)
    }

    override fun onActivityResumed(activity: Activity?) {
        startSession = System.currentTimeMillis().toDouble()
    }

    override fun onActivityStarted(activity: Activity?) {
        if(!shouldRecordScreenViews) {
            return
        }

        try {
            val packageManager = activity!!.packageManager
            val info: ActivityInfo = packageManager.getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
            val activityLabel = info.loadLabel(packageManager)
            pigeonClient.screen(activityLabel.toString(), Arguments.createMap())
        } catch (e: NameNotFoundException) {
            PigeonLog.d(TAG,"Activity Not Found: $e")
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
}
