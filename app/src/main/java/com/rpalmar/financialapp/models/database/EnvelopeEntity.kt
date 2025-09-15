package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType
import java.util.Date

@Entity(tableName = "envelope_table")
data class EnvelopeEntity(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("balance") val balance: Double,
    @ColumnInfo("status") val status: EnvelopStatus,
    @ColumnInfo("style") val style:StyleEntity,

    //GOAL DATA
    @ColumnInfo("goalAmount") val goalAmount: Double,
    @ColumnInfo("goalType") val goalType:GoalType,
    @ColumnInfo("goalDeadline") val goalDeadline: Double? = null,

    @ColumnInfo("parentEnvelopID") val parentEnvelopID:Int? = null,
    @ColumnInfo("currencyID") val currencyID:Int,
    @ColumnInfo("createAt") override val createAt: Date,
): IEntity, IHistorical
