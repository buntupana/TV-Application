package com.buntupana.tv_application.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recommendation")
data class RecommendationEntity(
    @PrimaryKey
    val recommendationId: String,
    val title: String,
    val imageSource: String
)
