package com.example.yelpclone.data.model.yelp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
Returns Yelp response
 */
data class YelpSearchResult(
    @SerializedName("total") val total: Int,
    @SerializedName("businesses") val restaurants: List<YelpBusinesses>
) : Serializable
