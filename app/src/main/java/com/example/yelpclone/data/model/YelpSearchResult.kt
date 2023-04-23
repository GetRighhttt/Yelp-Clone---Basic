package com.example.yelpclone.data.model

import com.google.gson.annotations.SerializedName

/*
Returns Yelp response
 */
data class YelpSearchResult(
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val restaurants: List<YelpRestaurants>
)
