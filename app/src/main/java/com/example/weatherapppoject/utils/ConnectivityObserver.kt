package com.example.weatherapppoject.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observer() : Flow<Status>

    enum class Status{
        AVILABLE,
        UN_AVAILABLE,
        LOSING,
        LOST
    }
}