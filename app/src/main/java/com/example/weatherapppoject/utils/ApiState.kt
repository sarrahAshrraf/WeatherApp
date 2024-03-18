package com.example.weatherapppoject.utils

import com.example.WeatherAppProject.WeatherList
import com.example.weatherapppoject.forecastmodel.WeatherResponse

sealed class ApiState {
    class Suceess(val data: WeatherResponse): ApiState()
    class Failure (val error: Throwable): ApiState()
    class Loading: ApiState()
}