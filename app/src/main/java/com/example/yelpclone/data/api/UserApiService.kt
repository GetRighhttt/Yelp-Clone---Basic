package com.example.yelpclone.data.api

import com.example.yelpclone.domain.model.users.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("size") size: Int
    ): Response<List<UserList>>
}