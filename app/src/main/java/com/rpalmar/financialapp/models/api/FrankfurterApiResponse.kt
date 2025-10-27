package com.rpalmar.financialapp.models.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class FrankfurterApiResponse(
    @SerializedName("amount") val amount:Int,
    @SerializedName("base") val base:String,
    @SerializedName("date") val date: Date,
    @SerializedName("rates") val rates: Map<String,Double>
)