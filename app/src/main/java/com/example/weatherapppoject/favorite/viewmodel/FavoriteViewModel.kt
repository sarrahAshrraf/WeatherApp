package com.example.weatherapppoject.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel (private val weatherRepository: WeatherRepositoryImpl) : ViewModel() {
    private val _favData = MutableStateFlow<DBState>(DBState.Loading())
    val currentWeather: StateFlow<DBState> = _favData

    fun addToFavorites(fav: WeatherResponse, long: Double, lat: Double) {
        viewModelScope.launch (Dispatchers.IO){
            weatherRepository.insertfavIntoDB(fav,long,lat)
            Log.i("=======", "addToFavorites: done")
//            withContext(Dispatchers.Main){
//                Toast.makeText(context,"item added",Toast.LENGTH_SHORT).show()
//            }
        }
    }

    fun removeFromFavorites( long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.deleteFromFav(long,lat)
        }
    }


    fun showFavItems() {
        viewModelScope.launch(Dispatchers.IO) {

            weatherRepository.getFavoriteData()
                .catch { exception ->
                    _favData.value = DBState.Failure(exception)
                }
                ///edits here==> products.collect
                .collect { data ->
                    _favData.value = DBState.Suceess(data)
                }
        }
    }

}




