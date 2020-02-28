@file:JvmName("DataUtils")
package io.pigeonapp

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Throws(JSONException::class)
fun convertMapToJson(readableMap: ReadableMap?): JSONObject {
    val jsonObject = JSONObject()
    val iterator = readableMap!!.keySetIterator()
    while (iterator.hasNextKey()) {
        val key = iterator.nextKey()
        when (readableMap.getType(key)) {
            ReadableType.Null -> jsonObject.put(key, JSONObject.NULL)
            ReadableType.Boolean -> jsonObject.put(key, readableMap.getBoolean(key))
            ReadableType.Number -> jsonObject.put(key, readableMap.getDouble(key))
            ReadableType.String -> jsonObject.put(key, readableMap.getString(key))
            ReadableType.Map -> jsonObject.put(key, convertMapToJson(readableMap.getMap(key)))
            ReadableType.Array -> jsonObject.put(key, convertArrayToJson(readableMap.getArray(key)))
        }
    }
    return jsonObject
}

@Throws(JSONException::class)
fun convertArrayToJson(readableArray: ReadableArray?): JSONArray {
    val jsonArray = JSONArray()
    for (i in 0 until readableArray!!.size()) {
        when (readableArray.getType(i)) {
            ReadableType.Null -> {}
            ReadableType.Boolean -> jsonArray.put(readableArray.getBoolean(i))
            ReadableType.Number -> jsonArray.put(readableArray.getDouble(i))
            ReadableType.String -> jsonArray.put(readableArray.getString(i))
            ReadableType.Map -> jsonArray.put(convertMapToJson(readableArray.getMap(i)))
            ReadableType.Array -> jsonArray.put(convertArrayToJson(readableArray.getArray(i)))
        }
    }
    return jsonArray
}
