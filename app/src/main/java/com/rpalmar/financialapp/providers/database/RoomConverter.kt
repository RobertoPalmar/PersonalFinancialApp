package com.rpalmar.financialapp.providers.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rpalmar.financialapp.models.database.StyleEntity
import java.util.Date

class RoomConverter {
    @TypeConverter
    fun fromStyle(style: StyleEntity): String {
        return Gson().toJson(style)
    }

    @TypeConverter
    fun toStyle(data: String): StyleEntity {
        val type = object : TypeToken<StyleEntity>() {}.type
        return Gson().fromJson(data, type)
    }

    @TypeConverter
    fun timestampToDate(value:Long?): Date?{
        return value?.let{ Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?):Long?{
        return date?.time?.toLong();
    }
}