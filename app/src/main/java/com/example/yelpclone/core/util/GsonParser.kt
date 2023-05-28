package com.example.yelpclone.core.util

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonParser(
    private val gson: Gson
): JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json, type)
    }

    override fun <T> toJson(obj: T, type: Type): String? {
        return gson.toJson(obj, type)
    }

    override fun <T> fromJsonDouble(json: Double, type: Type): T? {
        return gson.fromJson(json.toString(), type)
    }

    override fun <T> toJsonDouble(obj: T, type: Type): Double? {
        return gson.toJson(obj, type).toDouble()
    }
}