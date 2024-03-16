package com.example.weatherapppoject.repository

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse

interface WeatherRepositoryInter {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String
    ): WeatherList
    suspend fun getFiveDaysWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        apiKey: String,
        lang: String
    ): WeatherResponse

}