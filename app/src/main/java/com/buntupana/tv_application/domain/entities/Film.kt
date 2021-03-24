package com.buntupana.tv_application.domain.entities

data class Film(
    val filmId: String,
    val title: String,
    val coverResource: String,
    val slideShowResource: String,
    val plot: String,
    val duration: Long,
    val year: Int,
    val favourite: Boolean,
    val categoryList: List<Category>
)
