package com.example.weatherapppoject.map.viemodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryImpl

class MapViewModelFactory (
    val weathRepo: WeatherRepositoryImpl, private val geocoder: Geocoder,
    private val context: Context
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapFragmentViewModel::class.java)){
            MapFragmentViewModel(weathRepo, geocoder,context) as T}
        else{
            throw IllegalArgumentException("viewModel class notfound")
        }
    }

}