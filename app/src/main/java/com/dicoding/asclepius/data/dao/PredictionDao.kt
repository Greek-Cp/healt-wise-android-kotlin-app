package com.dicoding.asclepius.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.data.model.PredictionModel

@Dao
interface PredictionDao {
    @Insert
    suspend fun insert(prediction: PredictionModel)

    @Query("SELECT * FROM PredictionModel")
    suspend fun getAllPredictions(): List<PredictionModel>
}
