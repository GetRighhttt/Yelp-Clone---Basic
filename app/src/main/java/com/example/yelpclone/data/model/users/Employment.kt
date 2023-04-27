package com.example.yelpclone.data.model.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employment(
    val title: String,
    @SerializedName("key_skill") val skill: String
) : Parcelable
