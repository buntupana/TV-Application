package com.buntupana.tv_application.data.entities

import androidx.room.*

@Entity(tableName = "Film")
data class FilmEntity(
    @PrimaryKey
    val filmId: String,
    val title: String,
    val coverResource: String,
    val slideShowResource: String,
    val plot: String,
    val duration: Long,
    val year: Int
)

data class FilmAndFavourite(
    @Embedded val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "filmId"
    )
    val favourite: FavouriteEntity?
)

@Entity(primaryKeys = ["filmId", "categoryId"])
data class FilmCategoriesCrossRef(
    val filmId: String,
    val categoryId: Long
)

data class FilmAndCategories(
    @Embedded val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "categoryId",
        associateBy = Junction(FilmCategoriesCrossRef::class)
    )
    val categoryList: List<CategoryEntity>
)

data class FilmAndFavouriteAndCategories(
    @Embedded val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "filmId"
    )
    val favourite: FavouriteEntity?,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "categoryId",
        associateBy = Junction(FilmCategoriesCrossRef::class)
    )
    val categoryList: List<CategoryEntity>
)

@Entity(primaryKeys = ["filmId", "recommendationId"])
data class FilmRecommendationCrossRef(
    val filmId: String,
    val recommendationId: String
)

data class FilmWithRecommendations(
    @Embedded val film: FilmEntity,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "recommendationId",
        associateBy = Junction(FilmRecommendationCrossRef::class)
    )
    val recommendationList: List<RecommendationEntity>
)
