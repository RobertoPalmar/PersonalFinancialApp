package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.ISoftDelete

@Entity(tableName = "currency_table")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    //CURRENCY DATA
    @ColumnInfo("name") val name: String,
    @ColumnInfo("ISO") val ISO: String,
    @ColumnInfo("symbol") val symbol: String,
    @ColumnInfo("mainCurrency") val mainCurrency: Boolean,
    @ColumnInfo("currentExchangeRate") val currentExchangeRate: Double,
    @ColumnInfo("isDelete") override val isDelete: Boolean = false
): IEntity, ISoftDelete {
    fun toDomain(): CurrencyDomain{
        return CurrencyDomain(
            id = id,
            name = name,
            ISO = ISO,
            symbol = symbol,
            mainCurrency = mainCurrency,
            exchangeRate = currentExchangeRate
        )
    }
}