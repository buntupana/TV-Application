package com.buntupana.tv_application.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buntupana.tv_application.data.entities.FilmAndFavourite
import com.buntupana.tv_application.data.entities.FilmEntity
import com.buntupana.tv_application.data.entities.FilmRecommendationCrossRef
import com.buntupana.tv_application.data.entities.FilmWithRecommendations

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(filmEntity: FilmEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmList(filmEntityList: List<FilmEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilmRecommendationCrossRef(filmRecommendationCrossRefList: List<FilmRecommendationCrossRef>)

    @Transaction()
    @Query("SELECT * FROM Film")
    fun getFilmList(): LiveData<List<FilmAndFavourite>>

    @Query("SELECT * FROM Film WHERE filmId LIKE :filmId")
    fun getFilm(filmId: String): LiveData<FilmAndFavourite>

    @Transaction()
    @Query("SELECT * FROM Film WHERE filmId LIKE :filmId")
    fun getFilmWithRecommendations(filmId: String): LiveData<FilmWithRecommendations>
}