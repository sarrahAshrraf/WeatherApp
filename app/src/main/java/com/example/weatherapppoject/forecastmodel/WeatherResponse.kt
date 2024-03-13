package com.example.weatherapppoject.forecastmodel

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForeCastData>,
    val message: Int
)