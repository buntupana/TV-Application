package com.buntupana.tv_application.data.entities

import androidx.room.*

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

data class FavouriteAndFilmAndCategories(
    @Embedded val favourite: FavouriteEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "filmId"
    )
    val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "categoryId",
        associateBy = Junction(FilmCategoriesCrossRef::class)
    )
    val categoryList: List<CategoryEntity>
)