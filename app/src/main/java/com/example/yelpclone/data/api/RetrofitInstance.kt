package com.example.yelpclone.data.api

import com.example.yelpclone.core.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
Serves as a singleton for our retrofit instance.
 */
object RetrofitInstance {

    fun provideHttpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
        }.build()
        return client
    }

    private val gson: GsonConverterFactory = GsonConverterFactory.create()
    val retrofit: ApiService = Retrofit.Builder()
        .baseUrl(Constants.RESTAURANTS_BASE_URL)
        .addConverterFactory(gson)
        .client(provideHttpInterceptor())
        .build()
       .create(ApiService::class.java)
}

object UserRetrofitInstance {

    private val gson: GsonConverterFactory = GsonConverterFactory.create()
    val userRetrofit: UserService = Retrofit.Builder()
        .baseUrl(Constants.RANDOM_BASE_URL)
        .addConverterFactory(gson)
        .client(RetrofitInstance.provideHttpInterceptor())
        .build()
        .create(UserService::class.java)
}