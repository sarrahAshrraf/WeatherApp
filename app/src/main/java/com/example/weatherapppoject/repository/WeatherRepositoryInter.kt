package com.example.weatherapppoject.repository

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepositoryInter {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String
    ): Flow<WeatherList>
    suspend fun getFiveDaysWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): WeatherResponse

}