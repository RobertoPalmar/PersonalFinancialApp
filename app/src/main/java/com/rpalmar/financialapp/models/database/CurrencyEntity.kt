package com.rpalmar.financialapp.models.database

import androidx.compose.ui.unit.Dp
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.interfaces.IEntity
import java.math.BigDecimal

@Entity(tableName = "currency_table")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    //CURRENCY DATA
    @ColumnInfo("name") val name: String,
    @ColumnInfo("ISO") val ISO: String,
    @ColumnInfo("symbol") val symbol: String,
    @ColumnInfo("mainCurrency") val mainCurrency: Boolean,
    @ColumnInfo("exchangeRate") val exchangeRate: Double
): IEntity{
    fun toDomain(exchangeRate:Double = 0.0): CurrencyDomain{
        return CurrencyDomain(
            id = id,
            name = name,
            ISO = ISO,
            symbol = symbol,
            mainCurrency = mainCurrency,
            rateMode = rateMode,
            exchangeRate = exchangeRate
        )
    }
}