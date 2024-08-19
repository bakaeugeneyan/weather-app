package com.weather.application.data.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,
    @SerializedName("current_weather_units")
    val currentWeatherUnits: CurrentWeatherUnits,
    @SerializedName("elevation")
    val elevation: Int,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int
)