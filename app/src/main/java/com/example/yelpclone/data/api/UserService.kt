package com.example.yelpclone.data.api

import com.example.yelpclone.data.model.users.UserList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("users?size=100")
    suspend fun getUsers(): Response<List<UserList>>

    @GET("users?size=100")
    suspend fun searchUsers(@Query("q") query: String): Response<List<UserList>>
}