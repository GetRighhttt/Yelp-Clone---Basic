package com.example.yelpclone.data.model.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class UserCoordinates(
    val latitude: Double = Random.nextDouble(),
    val longitude: Double = Random.nextDouble(),
) : Parcelable