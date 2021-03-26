package com.buntupana.tv_application.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
class CategoryEntity(
    @PrimaryKey
    val categoryId: Long,
    val name: String
)