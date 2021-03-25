package com.buntupana.tv_application.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buntupana.tv_application.data.entities.*

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(filmEntity: FilmEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmList(filmEntityList: List<FilmEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmCategoriesCrossRef(filmCategoriesCrossRefList: List<FilmCategoriesCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmRecommendationCrossRef(filmRecommendationCrossRefList: List<FilmRecommendationCrossRef>)

    @Transaction()
    @Query("SELECT * FROM Film ORDER BY title")
    fun getFilmList(): LiveData<List<FilmAndFavouriteAndCategories>>

    @Transaction
    @Query("SELECT * FROM Film WHERE filmId LIKE :filmId")
    fun getFilm(filmId: String): LiveData<FilmAndFavouriteAndCategories>

    @Transaction()
    @Query("SELECT * FROM Film WHERE filmId LIKE :filmId")
    fun getFilmWithRecommendations(filmId: String): LiveData<FilmWithRecommendations>
}