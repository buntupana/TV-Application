package com.buntupana.tv_application.data.utils

import androidx.arch.core.util.Function
import com.buntupana.tv_application.data.entities.FilmAndFavourite
import com.buntupana.tv_application.data.entities.FilmEntity
import com.buntupana.tv_application.data.entities.RecommendationEntity
import com.buntupana.tv_application.data.raw.FilmRaw
import com.buntupana.tv_application.data.raw.RecommendationRaw
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
            raw.externalId,
            raw.name,
            cover,
            slideshow,
            raw.description,
            raw.duration,
            raw.year
        )
    }
}

class FilmModelMapper : Function<FilmAndFavourite, Film> {
    override fun apply(input: FilmAndFavourite): Film {
        return Film(
            input.film.filmId,
            input.film.title,
            input.film.coverResource,
            input.film.slideShowResource,
            input.film.plot,
            input.film.duration,
            input.film.year,
            input.favourite?.favourite ?: false
        )
    }
}

class RecommendationModelMapper : Function<RecommendationEntity, Recommendation> {
    override fun apply(input: RecommendationEntity): Recommendation {
        return Recommendation(
            input.recommendationId,
            input.title,
            input.imageSource
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