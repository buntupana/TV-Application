package com.buntupana.tv_application.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.buntupana.tv_application.data.entities.FilmEntity
import com.buntupana.tv_application.data.entities.FilmRecommendationCrossRef
import com.buntupana.tv_application.data.entities.RecommendationEntity

@Database(
    entities = [
        FilmEntity::class,
        RecommendationEntity::class,
        FilmRecommendationCrossRef::class,
        RecommendationEntity::class
    ],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun recommendationDao(): RecommendationDao

    companion object {
        private const val DATABASE_NAME = "tv-application-db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        }
    }
}