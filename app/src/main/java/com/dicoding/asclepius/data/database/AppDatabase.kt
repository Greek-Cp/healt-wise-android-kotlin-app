package com.dicoding.asclepius.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.dao.PredictionDao
import com.dicoding.asclepius.data.model.PredictionModel

@Database(entities = [PredictionModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun predictionDao(): PredictionDao
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "health_wise.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
