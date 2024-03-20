package com.example.weatherapppoject.forecastmodel


data class Precipitation (
    val probability: String,
    val unit: String? = null,
    val value: String? = null,
    val type: String? = null
)
