package com.rpalmar.financialapp.utils

import java.text.SimpleDateFormat
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
}