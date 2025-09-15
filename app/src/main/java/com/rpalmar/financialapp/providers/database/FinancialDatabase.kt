package com.rpalmar.financialapp.providers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)

abstract class FinancialDatabase: RoomDatabase() {

}