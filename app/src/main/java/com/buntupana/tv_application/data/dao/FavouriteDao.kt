package com.buntupana.tv_application.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.buntupana.tv_application.data.entities.FavouriteEntity

@Dao
interface FavouriteDao {

    @Query("UPDATE favourite SET favourite = :favorite WHERE filmId LIKE :filmId")
    suspend fun setFavouriteFilm(filmId: String, favorite: Boolean)

    @Query("SELECT favourite FROM favourite WHERE filmId LIKE :filmId")
    suspend fun getFavouriteFilm(filmId: String): Boolean

    @Query("SELECT * FROM favourite WHERE favourite = 1")
    fun getFavouriteCount(): LiveData<List<FavouriteEntity>>
}