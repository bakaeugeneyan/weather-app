package com.weather.application

import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val mainUiState by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        if (mainUiState.isLoading) {

        } else if (mainUiState.errorMsg.isNotEmpty()) {
            // Show error message
            Text(text = mainUiState.errorMsg)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                mainUiState.temperatureCelsius?.let { tempC ->
                    Text(text = "Temperature: $tempC°C")
                }
                mainUiState.temperatureFahrenheit?.let { tempF ->
                    Text(text = "Temperature: $tempF°F")
                }
                mainUiState.currentTime?.let { time ->
                    val formattedTime = formatTimeForLowerApi(time)
                    Text(text = "Current Date and Time: $formattedTime")
                }
                // Display daily weather
                Text(text = "Daily Weather:")
                mainUiState.dailyWeather?.let { daily ->
                    daily.time.forEachIndexed { index, day ->
                        Text(
                            text = "$day: Max: ${daily.temperature2mMax[index]}°F, " +
                                    "Min: ${daily.temperature2mMin[index]}°F"
                        )
                    }
                }
            }
        }
    }
}

fun formatTimeForLowerApi(timeString: String): String {
    return try {
        // Convert the time string from the API to a Date object
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date = sdf.parse(timeString)

        // Format the Date object to a more readable format
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        outputFormat.format(date)
    } catch (e: Exception) {
        timeString // Return the original string if parsing fails
    }
}
