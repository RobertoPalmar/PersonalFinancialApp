package com.rpalmar.financialapp.models.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BcvDollarApiResponse(
    @SerializedName("dollar") val dollar: Double,
    @SerializedName("date") val date: Date
)