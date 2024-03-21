package com.example.weatherapppoject.home.location

import android.location.Location

sealed class LocationState {
        class Success(val location: Location) : LocationState()
        class Failure(val message: Throwable) : LocationState()
        object Loading : LocationState()
}
