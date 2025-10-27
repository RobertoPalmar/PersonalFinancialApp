package com.rpalmar.financialapp.models.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.ExchangeRateType
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.IHistorical
import java.util.Date

@Entity(
    tableName = "exchange_rate_table",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["currencyID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val rate: Double,
    val currencyID: Long,
    val source:String,
    val type: ExchangeRateType,
    override val createAt: Date
) : IEntity, IHistorical
