package com.example.yelpclone.di

import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.data.api.YelpApiService
import com.example.yelpclone.data.api.YelpRetrofitInstance
import com.example.yelpclone.data.api.UserRetrofitInstance
import com.example.yelpclone.data.api.UserApiService
import com.example.yelpclone.data.api.RepositoryImpl
import com.example.yelpclone.data.api.RetrofitFactory
import com.example.yelpclone.data.api.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

/*
Dependency Injection module
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideYelpApiService(): Retrofit? = RetrofitFactory.determineInstance(YelpApiService::class.java)

    @Singleton
    @Provides
    fun provideUserApiService(): Retrofit? = RetrofitFactory.determineInstance(UserApiService::class.java)

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
