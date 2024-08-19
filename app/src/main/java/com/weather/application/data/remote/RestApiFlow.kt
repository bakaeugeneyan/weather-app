package com.weather.application.data.remote

import com.weather.application.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RestApiFlow() {
	private val retrofit2: Retrofit2 = Retrofit2()
	
//	suspend fun getWeather(
//		latitude: Double,
//		longitude: Double,
//		temperatureUnit: String = "celsius"
//	): Flow<Response<WeatherResponse>> =
//		flow {
//			val response = retrofit2.restApi.getWeather(latitude, longitude, temperatureUnit = temperatureUnit)
//
//			if (response.isSuccessful) {
//				val weatherResponse = response.body()
//
//				if (weatherResponse != null) {
//					emit(Response.success(weatherResponse))
//				} else {
//					emit(Response.error(response.code(), response.errorBody()!!))
//				}
//			} else {
//				emit(Response.error(response.code(), response.errorBody()!!))
//			}
//		}
//	private val retrofit2: Retrofit2 = Retrofit2.getInstance()
	
	suspend fun getWeatherInBothUnits(
		latitude: Double,
		longitude: Double
	): Flow<Pair<Response<WeatherResponse>, Response<WeatherResponse>>> = flow {
		val responseCelsius = retrofit2.restApi.getWeather(latitude, longitude, temperatureUnit = "celsius")
		val responseFahrenheit = retrofit2.restApi.getWeather(latitude, longitude, temperatureUnit = "fahrenheit")
		
		emit(Pair(responseCelsius, responseFahrenheit))
	}
	
	
}