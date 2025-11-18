package com.rpalmar.financialapp.providers.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_9_10 = object : Migration(9, 10) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `category_table` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `type` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `color` TEXT NOT NULL,
                `isDelete` INTEGER NOT NULL
            )
        """)

        database.execSQL("""
            ALTER TABLE `transaction_table` 
            ADD COLUMN `categoryID` INTEGER DEFAULT NULL 
            REFERENCES `category_table`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
        """)
    }
}
