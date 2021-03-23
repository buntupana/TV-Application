package com.buntupana.tv_application.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourite")
data class FavouriteEntity(
    @PrimaryKey
    val filmId: String,
    val favourite: Boolean
)