package com.example.yelpclone.domain.model.users

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserList(
    val id: Int,
    val password: String,
    @SerializedName("first_name") val firstname: String,
    @SerializedName("last_name") val lastName: String,
    val username: String,
    val email: String,
    val avatar: String,
    val gender: String,
    @SerializedName("phone_number") val phoneNumber: String,
    val employment: Employment,
    val address: UserAddress
    ): Parcelable
