package com.example.weatherapppoject.map.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryImpl

class MapViewModelFactory (
    val weathRepo: WeatherRepositoryImpl
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapFragmentViewModel::class.java)){
            MapFragmentViewModel(weathRepo) as T}
        else{
            throw IllegalArgumentException("viewModel class notfound")
        }
    }

}