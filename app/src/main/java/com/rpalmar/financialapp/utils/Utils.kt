package com.rpalmar.financialapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun getFullFormatDate(date: Date): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }
    fun getFormatHours(date: Date): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }
}