package com.buntupana.tv_application.data.datasources

import com.buntupana.tv_application.data.ResourcesProvider
import com.buntupana.tv_application.data.api.FilmsService
import com.buntupana.tv_application.data.raw.FilmResponse
import com.buntupana.tv_application.data.raw.FilmsResponse
import com.buntupana.tv_application.data.raw.RecommendationResponse
import com.buntupana.tv_application.domain.entities.Resource
import javax.inject.Inject

class FilmRemoteDataSource @Inject constructor(
    private val filmsService: FilmsService,
    resourcesProvider: ResourcesProvider
) : BaseDataSource(resourcesProvider) {

    suspend fun fetchFilmList(): Resource<FilmsResponse> {
        return getResult { filmsService.fetchFilmList() }
    }

    suspend fun fetchFilm(filmId: String): Resource<FilmResponse> {
        return getResult { filmsService.fetchFilm("${filmId}_PAGE_HD") }
    }

    suspend fun fetchRecommendationList(filmId: String): Resource<RecommendationResponse> {
        return getResult { filmsService.fetchRecommendationList("external_content_id:$filmId") }
    }
}