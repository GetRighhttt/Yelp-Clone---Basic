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
interface RetrofitApi {
    fun produceRetrofit(): Retrofit
}

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
val userApi = UserApiService::class.java
val yelpApi = YelpApiService::class.java

object YelpRetrofitInstance : RetrofitApi {
    override fun produceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.YELP_BASE_URL)
            .addConverterFactory(gson)
            .client(provideHttpInterceptor())
            .build()
    }
}

object UserRetrofitInstance : RetrofitApi {
    override fun produceRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.YELP_BASE_URL)
            .addConverterFactory(gson)
            .client(provideHttpInterceptor())
            .build()
    }
}

class RetrofitFactory {
    companion object {
        private val yelpApi = YelpApiService::class.java
        val userApi = UserApiService::class.java

        fun determineInstance(classType: Class<*>?): Retrofit? = when (classType) {
            yelpApi -> YelpRetrofitInstance.produceRetrofit()
            userApi -> UserRetrofitInstance.produceRetrofit()
            else -> {
                null
            }
        }
    }
}
