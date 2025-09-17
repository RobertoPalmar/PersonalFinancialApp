package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.IHistorical
import com.rpalmar.financialapp.models.interfaces.ITransaction
import java.util.Date

@Entity(tableName = "accounts_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("name") override val name: String,
    @ColumnInfo("description") override val description: String,
    @ColumnInfo("balance") val balance: Double,
    @ColumnInfo("currencyID") val currencyID: Long,
    @ColumnInfo("style") val style:StyleEntity?,
    @ColumnInfo("createAt") override val createAt: Date
): IEntity, IHistorical, ITransaction