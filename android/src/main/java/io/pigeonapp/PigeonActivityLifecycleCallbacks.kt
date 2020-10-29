package io.pigeonapp

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

class PigeonActivityLifecycleCallbacks private constructor() : Application.ActivityLifecycleCallbacks {
    private val pigeonClient: PigeonClient = PigeonClient.getInstance()
    private var startSession: Double = System.currentTimeMillis().toDouble()

    companion object {
        private var pigeonActivityLifecycleCallbacks: PigeonActivityLifecycleCallbacks? = null

        fun enablePigeonActivityLifecycleCallbacks(context: Context) {
            if (pigeonActivityLifecycleCallbacks != null) {
                return
            }

            pigeonActivityLifecycleCallbacks = PigeonActivityLifecycleCallbacks()
            val application = context as Application
            application.registerActivityLifecycleCallbacks(pigeonActivityLifecycleCallbacks)
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

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
}
