package com.buntupana.tv_application.data.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavouriteDao {

    @Query("UPDATE favourite SET favourite = :favorite WHERE filmId LIKE :filmId")
    suspend fun setFavouriteFilm(filmId: String, favorite: Boolean)

    @Query("SELECT favourite FROM favourite WHERE filmId LIKE :filmId")
    suspend fun getFavouriteFilm(filmId: String) : Boolean
}