package com.example.yelpclone.di

import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.data.api.YelpApiService
import com.example.yelpclone.data.api.RetrofitInstance
import com.example.yelpclone.data.api.UserRetrofitInstance
import com.example.yelpclone.data.api.UserApiService
import com.example.yelpclone.data.api.RepositoryImpl
import com.example.yelpclone.data.api.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/*
Dependency Injection module
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideYelpApiService(): YelpApiService = RetrofitInstance.retrofit

    @Singleton
    @Provides
    fun provideUserApiService(): UserApiService = UserRetrofitInstance.userRetrofit

    @Singleton
    @Provides
    fun provideYelpRepository(yelpApiService: YelpApiService): RepositoryImpl = RepositoryImpl(yelpApiService)

    @Singleton
    @Provides
    fun provideUserRepository(userApiService: UserApiService): UserRepositoryImpl =
        UserRepositoryImpl(userApiService)

    @Singleton
    @Provides
    fun providesDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val mainCD: CoroutineDispatcher
            get() = Dispatchers.Main
        override val ioCD: CoroutineDispatcher
            get() = Dispatchers.IO
        override val defaultCD: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfinedCD: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}
