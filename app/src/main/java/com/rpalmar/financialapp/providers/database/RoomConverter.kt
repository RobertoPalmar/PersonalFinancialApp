package com.rpalmar.financialapp.providers.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rpalmar.financialapp.models.database.StyleEntity
import java.util.Date

class RoomConverter {
    @TypeConverter
    fun fromStyle(style: StyleEntity?): String? {
        return style?.let{ Gson().toJson(it) }
    }

    @TypeConverter
    fun toStyle(json: String?): StyleEntity? {
        return json?.let { Gson().fromJson(it, StyleEntity::class.java) }
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