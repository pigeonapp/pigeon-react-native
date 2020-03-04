package io.pigeonapp

import android.util.Log

object PigeonLog {
    private enum class LogLevel(val code: Int) {
        INFO(1), DEBUG(0), OFF(-1);

        companion object {
            fun lookup(code: Int): LogLevel {
                return when (code) {
                    1 -> INFO
                    0 -> DEBUG
                    else -> OFF
                }
            }
        }
    }

    private var logLevel = LogLevel.OFF

    @JvmStatic
    fun setLogLevel(logLevel: Int) {
        PigeonLog.logLevel = LogLevel.lookup(logLevel)
    }

    @JvmStatic
    fun d(tag: String?, logText: String?) {
        if (logLevel.code >= LogLevel.DEBUG.code) {
            Log.d(tag, logText)
        }
    }

    fun i(tag: String?, logText: String?) {
        if (logLevel.code >= LogLevel.INFO.code) {
            Log.i(tag, logText)
        }
    }
}
