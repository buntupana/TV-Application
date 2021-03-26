package com.buntupana.tv_application.data.utils

import androidx.arch.core.util.Function
import com.buntupana.tv_application.data.entities.CategoryEntity
import com.buntupana.tv_application.data.entities.FavouriteEntity
import com.buntupana.tv_application.data.entities.FilmEntity
import com.buntupana.tv_application.data.entities.RecommendationEntity
import com.buntupana.tv_application.data.raw.FilmRaw
import com.buntupana.tv_application.data.raw.GenreEntity
import com.buntupana.tv_application.data.raw.RecommendationRaw
import com.buntupana.tv_application.domain.entities.Category
import com.buntupana.tv_application.domain.entities.Film
import com.buntupana.tv_application.domain.entities.Recommendation

class FilmEntityMapper : Function<FilmRaw, FilmEntity> {

    override fun apply(raw: FilmRaw): FilmEntity {
        // Trying to get proper cover and slideshow otherwise we'll get any available
        val cover =
            raw.attachments.firstOrNull { it.name == "COVER4_1" || it.name == "GENERIC_COVER4_1" }?.value
                ?: raw.attachments.firstOrNull()?.name ?: ""

        val slideshow = raw.attachments.firstOrNull { it.name == "APP_SLSHOW_1" }?.value
            ?: raw.attachments.firstOrNull { it.name.contains("APP_SLSHOW") }?.name
            ?: raw.attachments.firstOrNull()?.name ?: ""

        return FilmEntity(
            raw.assetExternalId,
            raw.name,
            cover,
            slideshow,
            raw.description,
            raw.duration,
            raw.year
        )
    }
}

class FilmModelMapper(
    private val favourite: FavouriteEntity?,
    private val categoryList: List<CategoryEntity>,
    private val imageResourceBaseUrl: String
) : Function<FilmEntity, Film> {
    override fun apply(input: FilmEntity): Film {

        val categoryList = categoryList.map { Category(it.categoryId, it.name) }

        return Film(
            input.filmId,
            input.title,
            imageResourceBaseUrl + input.coverResource,
            imageResourceBaseUrl + input.slideShowResource,
            input.plot,
            input.duration,
            input.year,
            favourite?.favourite ?: false,
            categoryList
        )
    }
}

class RecommendationModelMapper(
    private val imageResourceBaseUrl: String
) : Function<RecommendationEntity, Recommendation> {
    override fun apply(input: RecommendationEntity): Recommendation {
        return Recommendation(
            input.recommendationId,
            input.title,
            imageResourceBaseUrl + input.imageSource
        )
    }
}

class RecommendationEntityMapper : Function<RecommendationRaw, RecommendationEntity> {
    override fun apply(input: RecommendationRaw): RecommendationEntity {

        val cover =
            input.images.firstOrNull { it.name == "COVER4_1" || it.name == "GENERIC_COVER4_1" }?.value
                ?: input.images.firstOrNull()?.name ?: ""

        return RecommendationEntity(
            input.externalContentId,
            input.name,
            cover
        )
    }
}

class CategoryEntityMapper : Function<GenreEntity, CategoryEntity> {
    override fun apply(input: GenreEntity): CategoryEntity {
        var category = input.name.replace("Cine ", "")
        category = category.replace("Ciena ", "")
        return CategoryEntity(input.id, category)
    }
}