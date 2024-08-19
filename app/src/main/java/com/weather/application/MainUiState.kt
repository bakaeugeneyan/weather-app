package com.weather.application

data class MainUiState(
	val isLoading: Boolean = false,
	val errorMsg: String = "",
	val temperatureCelsius: String = "",
	val temperatureFahrenheit: Double? = null
)