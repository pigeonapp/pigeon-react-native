package io.pigeonapp

import android.app.Activity
import android.app.Application
import android.os.Bundle

class PigeonActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {
    private val pigeonClient: PigeonClient = PigeonClient.getInstance()
    private var startSession: Double = System.currentTimeMillis().toDouble()

    init {
        pigeonClient.trackAppStarted()
    }

    override fun onActivityPaused(activity: Activity?) {
        pigeonClient.trackAppSession(System.currentTimeMillis().toDouble() - startSession)
    }

    override fun onActivityResumed(activity: Activity?) {
        startSession = System.currentTimeMillis().toDouble()
    }

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
}
