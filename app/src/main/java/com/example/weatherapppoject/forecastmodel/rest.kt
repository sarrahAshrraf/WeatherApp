package com.example.weatherapppoject.forecastmodel



data class Symbol (
   val number: String,
    val name: String,
    val symbolVar: String
)

data class Temperature (
    val unit: Unit,
    val value: String,
    val min: String,
    val max: String
)

data class Visibility (
    val value: String
)

data class WindDirection (
    val deg: String,
    val code: String,
    val name: String
)

data class WindGust (
    val gust: String,
    val unit: String
)

data class WindSpeed (
    val mps: String,
    val unit: String,
    val name: String
)

data class WeatherdataLocation (
    val name: String,
    val type: String,
    val country: String,
    val timezone: String,
    val location: LocationLocation
)

data class LocationLocation (
    val altitude: String,
    val latitude: String,
    val longitude: String,
    val geobase: String,
    val geobaseid: String
)

data class Meta (
    val lastupdate: String,
    val calctime: String,
    val nextupdate: String
)

data class Sun (
    val rise: String,
    val set: String
)

data class FeelsLike (
    val value: String,
    val unit: Unit
)
