package com.example.weatherapppoject.utils

import com.example.weatherapppoject.forecastmodel.WeatherResponse

sealed class ForeCastApiState {
    class Suceess(val data: WeatherResponse): ForeCastApiState()
    class Failure (val error: Throwable): ForeCastApiState()
    class Loading: ForeCastApiState()
}