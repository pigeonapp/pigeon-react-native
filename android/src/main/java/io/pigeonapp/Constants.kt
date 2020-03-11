@file:JvmName("Constants")
package io.pigeonapp

import okhttp3.MediaType.Companion.toMediaType

const val PACKAGE_NAME = "io.pigeonapp"
const val DEFAULT_INSTANCE = "default"

val JSON = "application/json; charset=utf-8".toMediaType()

const val CONFIG_PUBLIC_KEY = "publicKey"
const val CONFIG_TRACK_APP_EXCEPTIONS = "trackAppExceptions"
const val CONFIG_TRACK_APP_LIFECYCLE_EVENTS = "trackAppLifecycleEvents"

const val INTERNAL_EVENT_APP_INSTALLED = "Application Installed"
const val INTERNAL_EVENT_APP_UPDATED = "Application Updated"
const val INTERNAL_EVENT_APP_OPENED = "Application Opened"
const val INTERNAL_EVENT_APP_BACKGROUNDED = "Application Backgrounded"
const val INTERNAL_EVENT_APP_CRASHED = "Application Crashed"

const val MESSAGE_FILTER_KEY = "pigeon_pn_type"

const val SHARED_PREFERENCES_PREFIX = PACKAGE_NAME
const val SHARED_PREFERENCES_KEY_BUILD = "$SHARED_PREFERENCES_PREFIX.$DEFAULT_INSTANCE.build"
const val SHARED_PREFERENCES_KEY_VERSION = "$SHARED_PREFERENCES_PREFIX.$DEFAULT_INSTANCE.version"

const val EVENT_QUEUE_DEFAULT_FLUSH_LIMIT = 20
const val EVENT_QUEUE_DEFAULT_FLUSH_INTERVAL = 72
