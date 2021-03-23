package com.buntupana.tv_application.data.raw

data class RecommendationResponse(
    val metadata: Metadata,
    val response: List<RecommendationRaw>
)

data class RecommendationRaw(
    val ContentProperties: List<Any>,
    val availabilities: List<Availability>,
    val contentType: String,
    val externalContentId: String,
    val genres: List<Genre>,
    val id: Int,
    val images: List<ImageX>,
    val name: String,
    val prLevel: Int,
    val prName: String,
    val ratersCount: Int,
    val rating: Int,
    val recommendationReasons: List<Any>,
    val type: String
)

data class Availability(
    val AvailabilityProperties: List<Any>,
    val categories: List<Category>,
    val endTime: Long,
    val images: List<Image>,
    val serviceId: String,
    val startTime: Long,
    val videoId: String
)

data class ImageX(
    val name: String,
    val value: String
)

data class Image(
    val name: String,
    val value: String
)

data class Genre(
    val externalId: String,
    val id: String,
    val name: String
)

data class Category(
    val categoryId: String,
    val categoryName: String
)