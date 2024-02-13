package com.example.yelpclone.core.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.yelpclone.domain.model.yelp.YelpCategories
import com.example.yelpclone.domain.model.yelp.YelpCoordinates
import com.example.yelpclone.domain.model.yelp.YelpLocations
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(private val jsonParser: JsonParser) {

    @TypeConverter
    fun toCategoriesJson(categories: List<YelpCategories>): String {
        return jsonParser.toJson(
            categories,
            object : TypeToken<List<YelpCategories>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromCategoriesJson(json: String): List<YelpCategories> {
        return jsonParser.fromJson<ArrayList<YelpCategories>>(
            json,
            object : TypeToken<ArrayList<YelpCategories>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toLocationsJson(categories: YelpLocations): String {
        return jsonParser.toJson(
            categories,
            object : TypeToken<YelpLocations>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromLocationsJson(json: String): YelpLocations {
        return jsonParser.fromJson<YelpLocations>(
            json,
            object : TypeToken<YelpLocations>() {}.type
        ) ?: YelpLocations("", "", "", "", "", "", "")
    }

    @TypeConverter
    fun toCoordinatesJson(coordinates: YelpCoordinates): Double {
        return jsonParser.toJsonDouble(
            coordinates,
            object : TypeToken<YelpCoordinates>() {}.type
        ) ?: 0.0
    }

    @TypeConverter
    fun fromCategoriesJson(json: Double): YelpCoordinates {
        return jsonParser.fromJsonDouble<YelpCoordinates>(
            json,
            object : TypeToken<YelpCoordinates>() {}.type
        ) ?: YelpCoordinates(0.0, 0.0)
    }
}