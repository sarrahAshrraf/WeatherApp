package com.example.weatherapppoject.forecastmodel


enum class Unit(val value: String) {
    Empty("%"),
    HPa("hPa"),
    Kelvin("kelvin");

    companion object {
        public fun fromValue(value: String): Unit = when (value) {
            "%"      -> Empty
            "hPa"    -> HPa
            "kelvin" -> Kelvin
            else     -> throw IllegalArgumentException()
        }
    }
}