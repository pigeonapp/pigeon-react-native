@file:JvmName("DataUtils")
package io.pigeonapp

import com.facebook.react.bridge.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONException
import java.lang.reflect.MalformedParametersException

@Throws(JSONException::class)
fun convertMapToJson(readableMap: ReadableMap?): JsonObject {
    val jsonObject = JsonObject()
    val iterator = readableMap!!.keySetIterator()
    while (iterator.hasNextKey()) {
        val key = iterator.nextKey()
        when (readableMap.getType(key)) {
            ReadableType.Null -> jsonObject.add(key, null)
            ReadableType.Boolean -> jsonObject.addProperty(key, readableMap.getBoolean(key))
            ReadableType.Number -> jsonObject.addProperty(key, readableMap.getDouble(key))
            ReadableType.String -> jsonObject.addProperty(key, readableMap.getString(key))
            ReadableType.Map -> jsonObject.add(key, convertMapToJson(readableMap.getMap(key)))
            ReadableType.Array -> jsonObject.add(key, convertArrayToJson(readableMap.getArray(key)))
        }
    }
    return jsonObject
}

@Throws(JSONException::class)
fun convertArrayToJson(readableArray: ReadableArray?): JsonArray {
    val jsonArray = JsonArray()
    for (i in 0 until readableArray!!.size()) {
        when (readableArray.getType(i)) {
            ReadableType.Null -> {}
            ReadableType.Boolean -> jsonArray.add(readableArray.getBoolean(i))
            ReadableType.Number -> jsonArray.add(readableArray.getDouble(i))
            ReadableType.String -> jsonArray.add(readableArray.getString(i))
            ReadableType.Map -> jsonArray.add(convertMapToJson(readableArray.getMap(i)))
            ReadableType.Array -> jsonArray.add(convertArrayToJson(readableArray.getArray(i)))
        }
    }
    return jsonArray
}

@Throws(Exception::class)
fun convertToWritableMap(map: Map<*, *>): WritableMap {
    val writableMap: WritableMap = WritableNativeMap()
    val iterator: Iterator<String> = map.keys.iterator() as Iterator<String>
    while (iterator.hasNext()) {
        val key = iterator.next()
        when (val value = map[key]) {
            Boolean -> writableMap.putBoolean(key, (value as Boolean?)!!)
            Int -> writableMap.putInt(key, (value as Int?)!!)
            Double -> writableMap.putDouble(key, (value as Double?)!!)
            String -> writableMap.putString(key, value as String?)
            else -> writableMap.putString(key, value.toString())
        }
    }
    return writableMap
}
