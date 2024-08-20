package com.weather.application

import com.weather.application.data.model.DailyWeather

data class MainUiState(
	val isLoading: Boolean = false,
	val errorMsg: String = "",
	val temperatureCelsius: String = "",
	val temperatureFahrenheit: Double? = null,
	val currentTime: String? = null,
	val dailyWeather: DailyWeather? = null,
)