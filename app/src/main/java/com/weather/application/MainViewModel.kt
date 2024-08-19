package com.weather.application

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.application.data.remote.RestApiFlow
import com.weather.application.data.remote.Retrofit2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
	private val repository: RestApiFlow = RestApiFlow()
) : ViewModel() {
	private val _uiState = MutableStateFlow(MainUiState())
	val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
	
	fun getWeather(latitude: Double, longitude: Double) {
		viewModelScope.launch {
			Log.d("MainViewModel", "Fetching weather data for lat: $latitude, long: $longitude")
			
			repository.getWeatherInBothUnits(latitude, longitude)
				.flowOn(Dispatchers.IO)
				.onStart {
					Log.d("MainViewModel", "Starting weather data fetch")
					_uiState.update { currentState ->
						currentState.copy(
							isLoading = true,
							errorMsg = ""
						)
					}
				}
				.onCompletion {
					Log.d("MainViewModel", "Completed weather data fetch")
					_uiState.update { currentState -> currentState.copy(isLoading = false) }
				}
				.catch {
					val errorMsg = Retrofit2.ErrorResponseHandling.exceptionHandling(it)
					Log.e("MainViewModel", "Error fetching weather data: $errorMsg")
					_uiState.update { currentState -> currentState.copy(errorMsg = errorMsg) }
				}
				.collect { (celsiusResponse, fahrenheitResponse) ->
					Log.d("MainViewModel", "Received weather data")
					
					if (celsiusResponse.isSuccessful && fahrenheitResponse.isSuccessful) {
						val weatherCelsius = celsiusResponse.body()!!
						val weatherFahrenheit = fahrenheitResponse.body()!!
						
						Log.d("MainViewModel", "Celsius temperature: ${weatherCelsius.currentWeather.temperature}")
						Log.d("MainViewModel", "Fahrenheit temperature: ${weatherFahrenheit.currentWeather.temperature}")
						
						
						// Update the UI state with both temperatures
						_uiState.update { currentState ->
							currentState.copy(
								temperatureCelsius = weatherCelsius.currentWeatherUnits.temperature,
								temperatureFahrenheit = weatherFahrenheit.currentWeather.temperature
							)
						}
					} else {
						val errorMsg = "Error fetching weather data"
						Log.e("MainViewModel", errorMsg)
						_uiState.update { currentState -> currentState.copy(errorMsg = errorMsg) }
					}
				}
		}
	}
}