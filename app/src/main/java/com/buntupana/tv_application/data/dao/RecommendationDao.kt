package com.buntupana.tv_application.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.buntupana.tv_application.data.entities.RecommendationEntity

@Dao
interface RecommendationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendationList(recommendationList: List<RecommendationEntity>)
}