package com.rpalmar.financialapp.views.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.R

val PlusJakartaSansFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_regular),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    ),
)
