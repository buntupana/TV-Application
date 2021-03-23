package com.buntupana.tv_application.di

import android.app.Application
import android.content.Context
import com.buntupana.tv_application.data.api.FilmsService
import com.buntupana.tv_application.data.dao.AppDatabase
import com.buntupana.tv_application.data.dao.FavouriteDao
import com.buntupana.tv_application.data.dao.FilmDao
import com.buntupana.tv_application.data.dao.RecommendationDao
import com.buntupana.tv_application.data.datasources.FilmRemoteDataSource
import com.buntupana.tv_application.data.repositories.FilmsRepositoryImpl
import com.buntupana.tv_application.domain.repositories.FilmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Singleton
    @Provides
    fun providePrivateOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideFilmsService(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): FilmsService {
        return provideService(
            okHttpClient,
            FilmsService::class.java,
            "https://smarttv.orangetv.orange.es/stv/api/",
            moshiConverterFactory
        )
    }

    private fun <T> provideService(
        okHttpClient: OkHttpClient,
        serviceClass: Class<T>,
        baseUrl: String,
        moshiConverterFactory: MoshiConverterFactory
    ): T {
        return createRetrofit(okHttpClient, baseUrl, moshiConverterFactory).create(serviceClass)
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application) = AppDatabase.getInstance(app)

    @Singleton
    @Provides
    fun provideFilmDao(db: AppDatabase) = db.filmDao()

    @Singleton
    @Provides
    fun provideRecommendationDao(db: AppDatabase) = db.recommendationDao()

    @Singleton
    @Provides
    fun provideFavouriteDao(db: AppDatabase) = db.favouriteDao()

    @Singleton
    @Provides
    fun provideFilmRepository(
        filmRemoteDataSource: FilmRemoteDataSource,
        filmDao: FilmDao,
        recommendationDao: RecommendationDao,
        favouriteDao: FavouriteDao

    ): FilmRepository {
        return FilmsRepositoryImpl(filmRemoteDataSource, filmDao, favouriteDao, recommendationDao)
    }
}