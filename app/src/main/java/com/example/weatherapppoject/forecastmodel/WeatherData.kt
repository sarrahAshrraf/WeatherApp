package com.example.weatherapppoject.forecastmodel

data class Weatherdata (
    val location: WeatherdataLocation,
    val credit: String,
    val meta: Meta,
    val sun: Sun,
    val forecast: Forecast
)