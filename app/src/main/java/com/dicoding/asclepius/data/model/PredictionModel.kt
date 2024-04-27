package com.dicoding.asclepius.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PredictionModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String,
    val predictionResults: String,
    val confidenceScores: Double,
    val imagePrediction: ByteArray?
    )
