package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.interfaces.IEntity
import java.util.UUID

@Entity(tableName = "transaction_table")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("transactionCode") val transactionCode: UUID,
    @ColumnInfo("originSourceID") val originSourceID: Long? = null,
    @ColumnInfo("originSourceType") val originSourceType:TransactionSourceType? = null,
    @ColumnInfo("destinationSourceID") val destinationSourceID: Long? = null,
    @ColumnInfo("destinationSourceType") val destinationSourceType:TransactionSourceType? = null,
    @ColumnInfo("amount") val amount: Double,
    @ColumnInfo("description") val description:String
): IEntity