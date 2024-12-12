package com.example.capstone.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DrowsinessRecord::class], version = 2, exportSchema = false)
abstract class DrowsinessDatabase : RoomDatabase() {
    abstract fun drowsinessDao(): DrowsinessDao

    companion object {
        @Volatile
        private var INSTANCE: DrowsinessDatabase? = null

        fun getDatabase(context: Context): DrowsinessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrowsinessDatabase::class.java,
                    "drowsiness_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
