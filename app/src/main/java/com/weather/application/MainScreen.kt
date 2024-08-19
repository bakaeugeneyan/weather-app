package com.weather.application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
	viewModel: MainViewModel = viewModel()
) {
	val mainUiState by viewModel.uiState.collectAsState()
	
	Scaffold { innerPadding ->
		if (mainUiState.isLoading) {
			// Show loading indicator
//			Box(
//				modifier = Modifier.fillMaxSize(),
//				contentAlignment = Alignment.Center
//			) {
//				CircularProgressIndicator()
//			}
		} else if (mainUiState.errorMsg.isNotEmpty()) {
			// Show error message
			Text(text = mainUiState.errorMsg)
		} else {
			// Show temperatures in both Celsius and Fahrenheit
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
			){
				mainUiState.temperatureCelsius?.let { tempC ->
					Text(text = "Temperature: $tempC°C")
				}
				mainUiState.temperatureFahrenheit?.let { tempF ->
					Text(text = "Temperature: $tempF°F")
				}
			}
		}
		
	}
	
}
