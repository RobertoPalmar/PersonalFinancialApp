package com.rpalmar.financialapp.utils

import com.rpalmar.financialapp.models.domain.auxiliar.DateRangeData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    fun getFullFormatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }

    fun getFormatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun getFormatHours(date: Date): String {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }

    fun getCurrentMonthDateRange():DateRangeData{
        // GET START MONTH DATE
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDate = calendar.time

        //GET LAST MONTH DATE
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, lastDay)
        val lastDate = calendar.time

        return DateRangeData(
            startDate = firstDate,
            endDate = lastDate
        )
    }

    /**
     * Calculate exchange rate. If destinationExchangeRate is null, set 1.0 by default
     */
    fun calculateExchangeRate(amountOrigin: Double, sourceExchangeRate: Double, destinationExchangeRate: Double = 1.0): Double{
        return amountOrigin / (sourceExchangeRate * destinationExchangeRate);
    }
}