package com.example.yelpclone.data.model

import com.google.gson.annotations.SerializedName

data class YelpLocation(
    val city: String,
    val country: String,
    val address2: String,
    val address3: String,
    val state: String,
    val address1: String,
    @SerializedName("zip_code") val zipCode: String
)
