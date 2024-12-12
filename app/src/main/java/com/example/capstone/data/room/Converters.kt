

package com.example.capstone.data.room

import androidx.room.TypeConverter
import java.io.File

class Converters {

    @TypeConverter
    fun fromFile(file: File?): String? {
        return file?.absolutePath
    }

    @TypeConverter
    fun toFile(filePath: String?): File? {
        return filePath?.let { File(it) }
    }
}
