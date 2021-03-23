package com.buntupana.tv_application.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.buntupana.tv_application.data.entities.FavouriteAndFilm
import com.buntupana.tv_application.data.entities.FavouriteEntity

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteList(favouriteEntityList: List<FavouriteEntity>)

    @Query("UPDATE favourite SET favourite = :favorite WHERE filmId LIKE :filmId")
    suspend fun setFavourite(filmId: String, favorite: Boolean)

    @Query("SELECT favourite FROM favourite WHERE filmId LIKE :filmId")
    fun getFavourite(filmId: String): Boolean

    @Query("SELECT COUNT(*) FROM favourite WHERE favourite = 1")
    fun getFavouriteCount(): LiveData<Int>

    @Transaction
    @Query("SELECT * FROM favourite WHERE favourite = 1")
    fun getFavouriteList(): LiveData<List<FavouriteAndFilm>>
}