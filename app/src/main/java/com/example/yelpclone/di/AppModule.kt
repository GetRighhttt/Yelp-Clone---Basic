package com.example.yelpclone.di

import android.app.Application
import com.example.yelpclone.data.api.RetrofitInstance
import com.example.yelpclone.data.api.YelpService
import com.example.yelpclone.domain.repository.RepositoryImpl
import com.example.yelpclone.domain.util.DispatcherProvider
import com.example.yelpclone.presentation.viewmodel.MainViewModelFactory
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
    /*
   Api service, Repo, and Dispatchers provides methods.
    */
    @Singleton
    @Provides
    fun provideApiService(): YelpService = RetrofitInstance.retrofit

    @Singleton
    @Provides
    fun provideRepository(yelpService: YelpService): RepositoryImpl = RepositoryImpl(yelpService)

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

    @Singleton
    @Provides
    fun provideCurrencyViewModelFactory(
        repository: RepositoryImpl,
        dispatcherProvider: DispatcherProvider
    ): MainViewModelFactory {
        return MainViewModelFactory(repository, dispatcherProvider)
    }
}
