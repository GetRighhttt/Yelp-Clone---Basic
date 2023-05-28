package com.example.yelpclone.core.util

import java.lang.reflect.Type

interface JsonParser {
    fun <T> fromJson(json: String, type: Type): T?
    fun <T> toJson(obj: T, type: Type): String?

    fun <T> fromJsonDouble(json: Double, type: Type): T?
    fun <T> toJsonDouble(obj: T, type: Type): Double?

}