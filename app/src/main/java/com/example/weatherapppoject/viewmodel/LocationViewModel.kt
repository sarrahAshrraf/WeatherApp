package com.example.weatherapppoject.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.util.Locale

class LocationViewModel(private val context: Context) : ViewModel() {
    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    fun getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        geocoder = Geocoder(context, Locale.getDefault())
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        task.addOnSuccessListener {
//            val geocoder = Geocoder(requireContext(),Locale.getDefault())
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                geocoder.getFromLocation(it.latitude,it.longitude,1,object: Geocoder.GeocodeListener{
                    override fun onGeocode(address: MutableList<Address>) {
//                            weatherList.weather[0].main.
                    }
                } )

            }

        }
    }
}