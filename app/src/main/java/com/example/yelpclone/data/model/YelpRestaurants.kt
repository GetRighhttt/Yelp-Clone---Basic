package com.example.yelpclone.data.model

import com.google.gson.annotations.SerializedName


data class YelpRestaurants(
    val rating: Int,
    val price: String,
    val phone: String,
    val id: String,
    val alias: String,
    @SerializedName("is_closed") val isOpen: Boolean,
    val categories: List<YelpCategories>,
    @SerializedName("review_count") val reviewCount: Int,
    val name: String,
    val url: String,
    @SerializedName("image_url") val imageUrl: String,
    val location: YelpLocation
)
