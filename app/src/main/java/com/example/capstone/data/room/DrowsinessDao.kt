package com.example.capstone.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DrowsinessDao {
    @Insert
    suspend fun insertRecord(record: DrowsinessRecord)

    @Query("SELECT * FROM drowsiness_records")
    suspend fun getAllRecords(): List<DrowsinessRecord>
}
