package com.dicoding.asclepius.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.data.model.PredictionModel

@Dao
interface PredictionDao {
    @Insert
    suspend fun insert(prediction: PredictionModel)

    @Query("SELECT * FROM PredictionModel")
    fun getAllPredictions(): LiveData<List<PredictionModel>>

    @Query("SELECT * FROM PredictionModel WHERE id = :predictionId")
    fun getPredictionById(predictionId: Int): LiveData<PredictionModel>
}