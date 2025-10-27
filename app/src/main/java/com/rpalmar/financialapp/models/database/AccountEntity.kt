package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.IHistorical
import com.rpalmar.financialapp.models.interfaces.IEntityTransaction
import com.rpalmar.financialapp.models.interfaces.ISoftDelete
import java.util.Date

@Entity(tableName = "accounts_table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("name") override val name: String,
    @ColumnInfo("description") override val description: String,
    @ColumnInfo("balance") override var balance: Double,
    @ColumnInfo("currencyID") val currencyID: Long,
    @ColumnInfo("style") val style:StyleEntity?,
    @ColumnInfo("isDelete") override val isDelete: Boolean = false,
    @ColumnInfo("createAt") override val createAt: Date,
): IEntity, IHistorical, ISoftDelete, IEntityTransaction