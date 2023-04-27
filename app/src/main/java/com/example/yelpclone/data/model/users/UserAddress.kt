package com.example.yelpclone.data.model.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAddress(
    val city: String,
    @SerializedName("street_name") val streetName: String,
    @SerializedName("street_number") val streetNumber: String,
    @SerializedName("zip_code") val zipCode: String,
    val state: String,
    val country: String
) : Parcelable