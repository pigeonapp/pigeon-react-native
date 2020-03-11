@file:JvmName("HttpClient")
package io.pigeonapp

import okhttp3.*

private val httpClient = OkHttpClient()

var baseUri = "https://api.pigeonapp.io/v1"

fun post(url:String, body: String, headers: Headers, callback: Callback) {
    val requestBody: RequestBody = RequestBody.create(JSON, body)

    val request: Request = Request.Builder()
        .url("$baseUri$url")
        .headers(headers)
        .post(requestBody)
        .build()

    httpClient.newCall(request).enqueue(callback)
}
