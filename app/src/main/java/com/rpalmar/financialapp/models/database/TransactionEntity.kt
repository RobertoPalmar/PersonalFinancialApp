package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.interfaces.IEntity
import java.util.Date
import java.util.UUID

@Entity(tableName = "transaction_table")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("transactionCode") val transactionCode: UUID,
    @ColumnInfo("sourceID") val sourceID: Long? = null,
    @ColumnInfo("sourceType") val sourceType:TransactionSourceType? = null,
    @ColumnInfo("amount") val amount: Double,
    @ColumnInfo("transactionType") val transactionType:TransactionType,
    @ColumnInfo("transactionDate") val transactionDate:Date,
    @ColumnInfo("currencyID") val currencyID: Long,
    @ColumnInfo("exchangeRate") val exchangeRate:Double,
    @ColumnInfo("description") val description:String,
    @ColumnInfo("linkedTransactionID") val linkedTransactionID: Long? = null,
    @ColumnInfo("isDelete") override val isDelete: Boolean = false,
): IEntity