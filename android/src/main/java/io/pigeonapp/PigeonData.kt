package io.pigeonapp

data class SaveContactRequest(
        val kind: String,
        val name: String,
        val value: String
)

data class TrackRequest(
        val customerUid: String,
        val event: String
)

data class GenericResponse(
        val success: Boolean
)