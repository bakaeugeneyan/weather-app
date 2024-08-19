package com.weather.application.data.model


import com.google.gson.annotations.SerializedName

data class CurrentWeatherUnits(
    @SerializedName("interval")
    val interval: String,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("temperature")
    val temperature: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("weathercode")
    val weathercode: String,
    @SerializedName("winddirection")
    val winddirection: String,
    @SerializedName("windspeed")
    val windspeed: String
)