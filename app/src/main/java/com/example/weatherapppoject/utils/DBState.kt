package com.example.weatherapppoject.utils

import com.example.weatherapppoject.forecastmodel.WeatherResponse

sealed class DBState {
    class Suceess(val data: List<WeatherResponse>): DBState()
    class Failure (val error: Throwable): DBState()
    class Loading: DBState()
}
