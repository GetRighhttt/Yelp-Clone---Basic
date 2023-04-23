package com.example.yelpclone.data.api

import com.example.yelpclone.domain.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson: GsonConverterFactory = GsonConverterFactory.create()
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(gson)
        .build()
}