package com.example.weatherapppoject.utils

import com.example.WeatherAppProject.WeatherList

sealed class ApiState {
    class Suceess(val data: WeatherList): ApiState()
    class Failure (val error: Throwable): ApiState()
    class Loading: ApiState()
}