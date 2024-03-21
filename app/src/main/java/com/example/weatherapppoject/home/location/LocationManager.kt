package com.example.weatherapppoject.home.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow

class LocationManager private constructor(context: Context){

    private var fusedLocationClient: FusedLocationProviderClient
    val location = MutableStateFlow<LocationState>(LocationState.Loading)

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    companion object{
        @Volatile
        private var INSTANCE : LocationManager? = null
        fun getInstance(context: Context) : LocationManager {
            return INSTANCE ?: synchronized(context){
                val instance = LocationManager(context)
                INSTANCE = instance
                instance
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        requestNewLocationData()
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0L).apply {
            setMinUpdateIntervalMillis(100)
        }.build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private val  locationCallback : LocationCallback = object : LocationCallback() {
        override fun onLocationResult( mLocationRequest: LocationResult) {
            val mLastLocation : Location? = mLocationRequest.lastLocation
            if (mLastLocation!=null){
                location.value = LocationState.Success(mLastLocation)
                removeLocationUpdate()
            }else{
                location.value = LocationState.Failure(Throwable())
            }
        }
    }

    private fun removeLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}