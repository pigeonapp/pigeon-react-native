@file:JvmName("DataUtils")
package io.pigeonapp

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONException

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
