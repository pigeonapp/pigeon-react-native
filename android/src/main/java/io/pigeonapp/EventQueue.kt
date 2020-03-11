package io.pigeonapp

import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.RequestBody
import okhttp3.internal.http2.Header

class EventQueue {
    val events = mutableListOf<BatchedTrackRequest>()
    val flushAt = EVENT_QUEUE_DEFAULT_FLUSH_LIMIT
    val flushInterval = EVENT_QUEUE_DEFAULT_FLUSH_INTERVAL
    private val gson = Gson()

    companion object {
        private var eventQueue: EventQueue? = null

        fun getInstance(): EventQueue {
            if(eventQueue != null) {
                return eventQueue as EventQueue
            }

            eventQueue = EventQueue()
            return eventQueue as EventQueue
        }
    }

    fun addEvent(batchedTrackRequest: BatchedTrackRequest) {
        events.add(batchedTrackRequest)
    }

    fun flushEvent() {

    }
}
