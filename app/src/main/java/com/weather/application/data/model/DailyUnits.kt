package com.weather.application.data.model


import com.google.gson.annotations.SerializedName

data class DailyUnits(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: String,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: String,
    @SerializedName("time")
    val time: String
)