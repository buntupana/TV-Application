package com.buntupana.tv_application.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Favourite")
data class FavouriteEntity(
    @PrimaryKey
    val filmId: String,
    val favourite: Boolean = false
)

data class FavouriteAndFilm(
    @Embedded val favourite: FavouriteEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "filmId"
    )
    val film: FilmEntity
)