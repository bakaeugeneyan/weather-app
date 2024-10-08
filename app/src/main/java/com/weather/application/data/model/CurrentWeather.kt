package com.weather.application.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("interval")
    val interval: Int,
    @SerializedName("is_day")
    val isDay: Int,
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("time")
    val time: String,
    @SerializedName("weathercode")
    val weathercode: Int,
    @SerializedName("winddirection")
    val winddirection: Int,
    @SerializedName("windspeed")
    val windspeed: Double
)