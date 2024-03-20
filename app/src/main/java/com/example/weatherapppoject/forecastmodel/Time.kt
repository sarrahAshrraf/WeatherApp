package com.example.weatherapppoject.forecastmodel


data class Time (
    val symbol: Symbol,
    val precipitation: Precipitation,
    val windDirection: WindDirection,
    val windSpeed: WindSpeed,
    val windGust: WindGust,
    val temperature: Temperature,
    val feelsLike: FeelsLike,
    val pressure: FeelsLike,
    val humidity: FeelsLike,
    val clouds: Clouds,
    val visibility: Visibility,
    val from: String,
    val to: String
)
