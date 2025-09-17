package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.IHistorical
import java.util.Date

@Entity(tableName = "currency_table")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("name") val name:String,
    @ColumnInfo("ISO") val ISO:String,
    @ColumnInfo("symbol") val symbol:String,
    @ColumnInfo("exchangeRate") val exchangeRate: Double,
    @ColumnInfo("currencyPriority") val currencyPriority:Int,
    @ColumnInfo("createAt") override val createAt: Date
): IEntity, IHistorical {
    fun toDomain(): CurrencyDomain{
        return CurrencyDomain(
            id = id,
            name = name,
            ISO = ISO,
            symbol = symbol,
            exchangeRate = exchangeRate,
            currencyPriority = currencyPriority,
        )
    }
}