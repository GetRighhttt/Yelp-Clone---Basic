package com.example.yelpclone.di

import android.app.Application
import androidx.room.Room
import com.example.yelpclone.data.api.RetrofitInstance
import com.example.yelpclone.data.api.ApiService
import com.example.yelpclone.domain.RepositoryImpl
import com.example.yelpclone.core.util.DispatcherProvider
import com.example.yelpclone.data.api.UserRetrofitInstance
import com.example.yelpclone.data.api.UserService
import com.example.yelpclone.data.db.BusinessDAO
import com.example.yelpclone.data.db.BusinessDatabase
import com.example.yelpclone.domain.UserRepositoryImpl
import com.example.yelpclone.domain.sot.SavedRepository
import com.example.yelpclone.presentation.viewmodel.main.MainViewModelFactory
import com.example.yelpclone.presentation.viewmodel.main.user.UserViewModelFactory
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
    fun provideYelpApiService(): ApiService = RetrofitInstance.retrofit

    @Singleton
    @Provides
    fun provideUserApiService(): UserService = UserRetrofitInstance.userRetrofit

    @Singleton
    @Provides
    fun provideYelpRepository(apiService: ApiService, businessDAO: BusinessDAO): RepositoryImpl = RepositoryImpl(apiService, businessDAO)

    @Singleton
    @Provides
    fun provideUserRepository(userApiService: UserService): UserRepositoryImpl =
        UserRepositoryImpl(userApiService)

    @Singleton
    @Provides
    fun provideSavedRepository(businessDAO: BusinessDAO) = SavedRepository(businessDAO)

    @Singleton
    @Provides
    fun provideBusinessDatabase(app: Application): BusinessDatabase = Room.databaseBuilder(
        app, BusinessDatabase::class.java,
        "business_db"
    ).fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDAO(database: BusinessDatabase): BusinessDAO = database.getBusinessDAO()

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
    fun provideMainViewModelFactory(
        repository: RepositoryImpl,
        dispatcherProvider: DispatcherProvider
    ): MainViewModelFactory {
        return MainViewModelFactory(repository, dispatcherProvider)
    }

    @Singleton
    @Provides
    fun provideUserViewModelFactory(
        repository: UserRepositoryImpl,
        dispatcherProvider: DispatcherProvider
    ): UserViewModelFactory {
        return UserViewModelFactory(repository, dispatcherProvider)
    }
}
