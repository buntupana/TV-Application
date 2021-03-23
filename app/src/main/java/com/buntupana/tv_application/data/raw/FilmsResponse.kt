package com.buntupana.tv_application.data.raw

data class FilmResponse(
    val metadata: Metadata,
    val response: FilmRaw
)

data class FilmsResponse(
    val metadata: Metadata,
    val response: List<FilmRaw>
)

data class Metadata(
    val fullLength: Int = 0,
    val request: String,
    val timestamp: Long
)

data class FilmRaw(
    val adsInfo: String,
    val advisories: String,
    val allowedTerminalCategories: List<AllowedTerminalCategory>,
    val assetExternalId: String,
    val assetId: Long,
    val attachments: List<Attachment>,
    val awards: List<Any>,
    val broadcastTime: Int,
    val categoryId: Int,
    val chapters: List<Any>,
    val contentProvider: String,
    val contentProviderExternalId: String,
    val definition: String,
    val description: String,
    val discountId: String,
    val duration: Long,
    val encodings: List<Encoding>,
    val episodeId: String,
    val externalChannelId: String,
    val externalId: String,
    val extrafields: List<Extrafield>,
    val flags: Int,
    val genreEntityList: List<GenreEntity>,
    val genres: List<Int>,
    val id: Int,
    val isSecured: Boolean,
    val keywords: String,
    val metadata: List<MetadataX>,
    val name: String,
    val plannedPublishDate: Any,
    val prLevel: Int,
    val prName: String,
    val pricingMatrixId: Int,
    val removalDate: Long,
    val rentalPeriod: String,
    val rentalPeriodUnit: String,
    val responseElementType: String,
    val reviewerRating: String,
    val reviews: List<Any>,
    val securityGroups: List<Any>,
    val seriesName: String,
    val seriesNumberOfEpisodes: String,
    val seriesSeason: String,
    val shortName: String,
    val simultaneousViewsLimit: String,
    val status: Int,
    val template: String,
    val tvShowReference: TvShowReference,
    val type: String,
    val windowEnd: Long,
    val windowStart: Long,
    val year: Int
)

data class AllowedTerminalCategory(
    val externalId: String,
    val maxTerminals: Int,
    val maxTerminalsOfNonOperator: Int,
    val name: String,
    val responseElementType: String
)

data class Attachment(
    val assetId: Any,
    val assetName: String,
    val name: String,
    val responseElementType: String,
    val value: String
)

data class Encoding(
    val name: String,
    val responseElementType: String
)

data class Extrafield(
    val name: String,
    val responseElementType: String,
    val value: String
)

data class GenreEntity(
    val externalId: String,
    val id: Int,
    val name: String,
    val parentName: String,
    val responseElementType: String
)

data class MetadataX(
    val name: String,
    val responseElementType: String,
    val value: String
)

class TvShowReference(
)