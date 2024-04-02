package com.example.weatherapppoject.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapppoject.repository.WeatherRepositoryImpl

class FavoriteViewModelFactory ( val weathRepo: WeatherRepositoryImpl
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            FavoriteViewModel(weathRepo) as T}
        else{
            throw IllegalArgumentException("viewModel class not found")
        }
    }

}