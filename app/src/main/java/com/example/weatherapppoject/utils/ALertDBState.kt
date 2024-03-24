package com.example.weatherapppoject.utils

import com.example.weatherapppoject.onecall.model.OneApiCall

sealed class  ALertDBState{
        class Suceess(val alertdata: List<OneApiCall>): ALertDBState()
        class Failure (val error: Throwable): ALertDBState()
        class Loading: ALertDBState()
    }