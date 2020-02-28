package io.pigeonapp

import com.facebook.react.bridge.ReadableMap

data class SaveContactRequest(
    val kind: String,
    val name: String,
    val value: String
)

data class TrackRequest(
    val customerUid: String,
    val event: String,
    val data: ReadableMap
)

data class GenericResponse(
    val success: Boolean
)
