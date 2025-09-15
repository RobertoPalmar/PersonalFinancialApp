package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.TransactionSourceType
import java.util.Date
import java.util.UUID

data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    val transactionCode: UUID,
    val originSourceID:Int? = null,
    val originSourceType:TransactionSourceType? = null,
    val destinationSourceID:Int? = null,
    val destinationSourceType:TransactionSourceType? = null,
): IEntity