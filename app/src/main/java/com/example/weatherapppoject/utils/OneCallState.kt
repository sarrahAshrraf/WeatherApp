package com.example.weatherapppoject.utils

import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.onecall.model.OneApiCall

sealed class OneCallState {
    class Suceess(val data: OneApiCall): OneCallState()
    class Failure (val error: Throwable): OneCallState()
    class Loading: OneCallState()
}