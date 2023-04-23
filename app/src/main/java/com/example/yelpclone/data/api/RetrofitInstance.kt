package com.example.yelpclone.data.api

import com.example.yelpclone.domain.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
Serves as a singleton for our retrofit instance.
 */
object RetrofitInstance {

    private fun provideHttpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        /**
         * Now we create an OKHTTPClient Instance.
         *
         * And we will show how to manually do connection timeouts just in case
         * somebody has slow internet.
         */
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
        }.build()

        return client
    }

    private val gson: GsonConverterFactory = GsonConverterFactory.create()
    val retrofit: YelpService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(gson)
        .client(provideHttpInterceptor())
        .build()
       .create(YelpService::class.java)
}