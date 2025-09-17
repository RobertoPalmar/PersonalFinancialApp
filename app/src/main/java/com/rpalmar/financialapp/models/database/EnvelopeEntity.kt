package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.IHistorical
import com.rpalmar.financialapp.models.interfaces.ITransaction
import java.util.Date

@Entity(tableName = "envelope_table")
data class EnvelopeEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("name") override val name: String,
    @ColumnInfo("description") override val description: String,
    @ColumnInfo("balance") val balance: Double,
    @ColumnInfo("status") val status: EnvelopStatus,
    @ColumnInfo("style") val style:StyleEntity?,

    //GOAL DATA
    @ColumnInfo("goalAmount") val goalAmount: Double,
    @ColumnInfo("goalType") val goalType:GoalType,
    @ColumnInfo("goalDeadline") val goalDeadline: Double? = null,

    @ColumnInfo("parentEnvelopID") val parentEnvelopID: Long? = null,
    @ColumnInfo("currencyID") val currencyID: Long,
    @ColumnInfo("createAt") override val createAt: Date,
): IEntity, IHistorical, ITransaction
