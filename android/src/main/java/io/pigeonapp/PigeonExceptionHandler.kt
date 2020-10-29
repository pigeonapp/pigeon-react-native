package io.pigeonapp

class PigeonExceptionHandler(private val mainExceptionHandler: Thread.UncaughtExceptionHandler)
    : Thread.UncaughtExceptionHandler {
    private val pigeonClient: PigeonClient = PigeonClient.getInstance()

    companion object {
        fun enablePigeonExceptionHandler() {
            val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
            if (defaultExceptionHandler is PigeonExceptionHandler) {
                return
            }

            val pigeonExceptionHandler = PigeonExceptionHandler(defaultExceptionHandler)
            Thread.setDefaultUncaughtExceptionHandler(pigeonExceptionHandler)
        }
    }

    override fun uncaughtException(thread: Thread?, exception: Throwable?) {
        pigeonClient.trackAppCrashed(exception)
        mainExceptionHandler.uncaughtException(thread, exception)
    }
}
