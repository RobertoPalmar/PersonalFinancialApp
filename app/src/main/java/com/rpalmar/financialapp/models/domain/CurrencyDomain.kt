package com.rpalmar.financialapp.models.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.interfaces.IDomain
import java.util.Date

data class CurrencyDomain(
    val id: Long = 0,
    val name:String,
    val ISO:String,
    val symbol:String,
    val exchangeRate: Double,
    val currencyPriority:Int,
): IDomain {
    override fun toEntity(): CurrencyEntity{
        return CurrencyEntity(
            id = id,
            name = name,
            ISO = ISO,
            symbol = symbol,
            exchangeRate = exchangeRate,
            currencyPriority = currencyPriority,
            createAt = Date(),
            isDelete = false
        )
    }
}