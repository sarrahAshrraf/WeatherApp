package com.example.weatherapppoject.forecastmodel

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: MutableList<ForeCastData>,
    val message: Int
)