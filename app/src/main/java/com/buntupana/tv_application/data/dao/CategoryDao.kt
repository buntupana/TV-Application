package com.buntupana.tv_application.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.buntupana.tv_application.data.entities.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryList(categoryEntityList: List<CategoryEntity>)
}