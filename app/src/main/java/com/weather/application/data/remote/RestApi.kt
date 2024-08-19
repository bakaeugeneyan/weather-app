package com.weather.application.data.remote

import com.weather.application.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
	@GET("v1/forecast")
	suspend fun getWeather(
		@Query("latitude") latitude: Double,
		@Query("longitude") longitude: Double,
		@Query("current_weather") currentWeather: Boolean = true,
		@Query("temperature_unit") temperatureUnit: String = "celsius"
	): Response<WeatherResponse>
}