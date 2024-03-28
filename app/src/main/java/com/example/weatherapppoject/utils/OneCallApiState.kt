package com.example.weatherapppoject.utils

import com.example.weatherapppoject.onecall.model.OneApiCall

sealed class OneCallApiState {
    class Suceess(val data: OneApiCall): OneCallApiState()
    class Failure (val error: Throwable): OneCallApiState()
    class Loading: OneCallApiState()
}