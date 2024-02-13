package com.example.yelpclone.domain.model.yelp

import java.io.Serializable

data class YelpCoordinates(
    val latitude: Double,
    val longitude: Double
) : Serializable
