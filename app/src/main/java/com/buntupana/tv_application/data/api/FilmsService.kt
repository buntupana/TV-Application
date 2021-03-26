package com.buntupana.tv_application.data.api

import com.buntupana.tv_application.data.raw.FilmResponse
import com.buntupana.tv_application.data.raw.FilmsResponse
import com.buntupana.tv_application.data.raw.RecommendationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmsService {

    @GET("rtv/v1/GetUnifiedList?client=json&statuses=published&definitions=SD;HD;4K&external_category_id=SED_3880&filter_empty_categories=true")
    suspend fun fetchFilmList(): Response<FilmsResponse>

    @GET("rtv/v1/GetVideo?client=json ")
    suspend fun fetchFilm(@Query("external_id") filmId: String): Response<FilmResponse>

    @GET("reco/v1/GetVideoRecommendationList?client=json&type=all&subscription=false&filter_viewed_content=true&max_results=10&blend=ar_od_blend_2424video&max_pr_level=8&quality=SD,HD&services=2424VIDEO")
    suspend fun fetchRecommendationList(@Query("params") filmId: String): Response<RecommendationResponse>
}