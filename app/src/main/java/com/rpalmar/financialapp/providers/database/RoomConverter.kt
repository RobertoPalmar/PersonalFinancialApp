package com.rpalmar.financialapp.providers.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.database.StyleEntity
import java.math.BigDecimal
import java.util.Date

class RoomConverter {

    //STYLE
    @TypeConverter
    fun fromStyle(style: StyleEntity?): String? {
        return style?.let{ Gson().toJson(it) }
    }

    @TypeConverter
    fun toStyle(json: String?): StyleEntity? {
        return json?.let { Gson().fromJson(it, StyleEntity::class.java) }
    }

    //TIMESTAMP
    @TypeConverter
    fun timestampToDate(value:Long?): Date?{
        return value?.let{ Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?):Long?{
        return date?.time?.toLong();
    }

    //BIG DECIMAL
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    //RATE MODE
    @TypeConverter
    fun fromRateMode(rateMode: RateMode): String {
        return rateMode.name
    }

    @TypeConverter
    fun toRateMode(value: String): RateMode {
        return RateMode.valueOf(value)
    }
}