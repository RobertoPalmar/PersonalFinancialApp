package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "accounts_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("balance") val balance: Double,
    @ColumnInfo("currencyID") val currencyID:Int,
    @ColumnInfo("style") val style:StyleEntity,
    @ColumnInfo("createAt") override val createAt: Date
): IEntity, IHistorical