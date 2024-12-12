package com.example.capstone.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.File

@Entity(tableName = "drowsiness_records")
@TypeConverters(Converters::class)
data class DrowsinessRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image: File,
    val timestamp: String,
    val message: String
)