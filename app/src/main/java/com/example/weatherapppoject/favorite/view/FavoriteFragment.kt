package com.example.weatherapppoject.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room

import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.AppDB
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.view.FiveDaysAdapter
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private lateinit var floatingActionButton : FloatingActionButton
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var homeFactory : HomeFragmentViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)

        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        homeFactory = HomeFragmentViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeFragmentViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingActionButton  = view.findViewById(R.id.floatingActionButton)



        floatingActionButton.setOnClickListener {
//            val db = Room.databaseBuilder(requireContext(), AppDB::class.java, "rr").build()
            Log.i("==set Onclcik===", "")

            replaceFragments(MapsFragment())
        }


        val longlat = sharedPreferencesManager.getLocationFromMap(SharedKey.GPS.name)
        val longg = longlat!!.first
        val latt = longlat.second
        Log.i("==latttt longggg===", ""+ longg+ latt)

//        lifecycleScope.launch(Dispatchers.Main) {
//
//            homeViewModel.fiveDaysWeather.collectLatest { weatherResponse ->
//                when (weatherResponse) {
//                    is ApiState.Suceess -> {
//                        Log.i("==apiStatecorotine===", ""+weatherResponse.data+ longg+ latt)
////                            favoriteViewModel.addToFavorites(weatherResponse.data, longg, latt)
//                        Log.i("==api succc===", ""+weatherResponse.data+ longg+ latt)
//
////                            CoroutineScope(Dispatchers.IO).launch {
//////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data, longg, latt)
////                                Log.i("==apiStatecorotine===", "coroutine")
////                                Log.i("==apiStatecorotine===", ""+weatherResponse.data+ longg+ latt)
////
////                                favoriteViewModel.addToFavorites(weatherResponse.data, longg, latt)
////                            }
//                    }
//                    is ApiState.Failure -> {
//                        Log.i("=====", "Failure in favorite API: ${weatherResponse.error}")
//                        // Handle the failure condition here
//                    }
//                    else -> {
//                        Log.i("=====", "Else in favorite API")
//                        // Handle any other states here
//                    }
//                }
//            }
//        }

        lifecycleScope.launch(Dispatchers.Main) {

            homeViewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ApiState.Suceess -> {
                        Log.i("======API success", ""+weatherResponse.data.list[0].dt_txt)

                        val forecastList = weatherResponse.data.list
                        val forecastItems = forecastList
                            .take(8)// Display only the first 5 forecast items

                    }

                    is ApiState.Loading -> {
                        Log.i("=====API LOADING", "")

                    }

                    else -> {
                        Log.i("==FAILIR===", "Failure in  API:")

                    }
                }
            }


        }


    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}