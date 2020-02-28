package io.pigeonapp

import org.json.JSONObject

data class SaveContactRequest(
    val kind: String,
    val name: String,
    val value: String
)

data class TrackRequest(
    val event: String,
    val data: JSONObject
)

data class GenericResponse(
    val success: Boolean
)
