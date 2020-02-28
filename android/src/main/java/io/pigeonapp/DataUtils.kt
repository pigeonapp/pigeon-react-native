package io.pigeonapp

import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import java.lang.reflect.MalformedParametersException

@Throws(MalformedParametersException::class)
fun convertToWritableMap(map: Map<*, *>): WritableMap {
    val writableMap: WritableMap = WritableNativeMap()
    val iterator: Iterator<String> = map.keys.iterator() as Iterator<String>
    while (iterator.hasNext()) {
        val key = iterator.next()
        when (val value = map[key]) {
            is Boolean -> {
                writableMap.putBoolean(key, (value as Boolean?)!!)
            }
            is Int -> {
                writableMap.putInt(key, (value as Int?)!!)
            }
            is Double -> {
                writableMap.putDouble(key, (value as Double?)!!)
            }
            is String -> {
                writableMap.putString(key, value as String?)
            }
            else -> {
                writableMap.putString(key, value.toString())
            }
        }
    }
    return writableMap
}