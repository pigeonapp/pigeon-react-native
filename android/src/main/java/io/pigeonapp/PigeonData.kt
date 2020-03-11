package io.pigeonapp

import com.google.gson.JsonObject

data class SaveContactRequest(
    val kind: String,
    val name: String,
    val value: String
)

data class TrackRequest(
    val event: String,
    val data: JsonObject
)

data class BatchedTrackRequest(
    val events: String,
    val timestamp: Long,
    val data: JsonObject
)

data class GenericResponse(
    val success: Boolean
)
