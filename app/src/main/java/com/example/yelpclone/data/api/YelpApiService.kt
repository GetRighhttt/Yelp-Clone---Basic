package com.example.yelpclone.data.api

import com.example.yelpclone.domain.model.yelp.YelpSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpApiService {

    @GET("businesses/search")
    suspend fun searchBusinesses(
        @Header("Authorization") authHeader: String,
        @Query("term") searchTerm: String,
        @Query("location") location: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ) : Response<YelpSearchResult>
}