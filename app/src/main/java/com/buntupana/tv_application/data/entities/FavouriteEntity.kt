package com.buntupana.tv_application.data.entities

import androidx.room.Entity

@Entity(tableName = "favourite")
data class FavouriteEntity(
    val filmId: String,
    val favourite: Boolean
)