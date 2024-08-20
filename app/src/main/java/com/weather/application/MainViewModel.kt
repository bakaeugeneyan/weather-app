package com.weather.application

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
    private val repository: RestApiFlow = RestApiFlow(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {

            repository.getWeatherInBothUnits(latitude, longitude)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = true,
                            errorMsg = ""
                        )
                    }
                }
                .onCompletion {
                    _uiState.update { currentState -> currentState.copy(isLoading = false) }
                }
                .catch {
                    val errorMsg = Retrofit2.ErrorResponseHandling.exceptionHandling(it)
                    _uiState.update { currentState -> currentState.copy(errorMsg = errorMsg) }
                }
                .collect { (celsiusResponse, fahrenheitResponse) ->

                    if (celsiusResponse.isSuccessful && fahrenheitResponse.isSuccessful) {
                        val weatherCelsius = celsiusResponse.body()!!
                        val weatherFahrenheit = fahrenheitResponse.body()!!
                        val currentTime = weatherCelsius.currentWeather.time
                        val dailyWeather = weatherFahrenheit.dailyWeather

                        // Update the UI state
                        _uiState.update { currentState ->
                            currentState.copy(
                                temperatureCelsius = weatherCelsius.currentWeatherUnits.temperature,
                                temperatureFahrenheit = weatherFahrenheit.currentWeather.temperature,
                                currentTime = currentTime,
                                dailyWeather = dailyWeather
                            )
                        }
                    } else {
                        val errorMsg = "Error fetching weather data"
                        _uiState.update { currentState -> currentState.copy(errorMsg = errorMsg) }
                    }
                }
        }
    }
}