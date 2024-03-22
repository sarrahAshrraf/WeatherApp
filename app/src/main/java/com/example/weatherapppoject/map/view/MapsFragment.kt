package com.example.weatherapppoject.map.view

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentMapsBinding
import com.example.weatherapppoject.favorite.view.FavoriteFragment
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.home.view.HomeFragment
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {
    private lateinit var geocoder: Geocoder
    private lateinit var binding: FragmentMapsBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var sharedPrefrencesManager: SharedPrefrencesManager
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var homeFactory : HomeFragmentViewModelFactory
//    private lateinit var latLng: LatLng
    private var lati = 0.0
    private var longgi =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefrencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository= WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)

        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        homeFactory = HomeFragmentViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, homeFactory).get(HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Places.initialize(requireContext(), Utils.MAPS_KEY)
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        geocoder = Geocoder(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            googleMap = map
            googleMap.setOnMapClickListener { latLng ->
                val locationName = getAddressFromLatLng(latLng)
                addMarkerToMap(latLng, locationName)
            }
        }

//
//        val longlat = sharedPrefrencesManager.getLocationFromMap(SharedKey.GPS.name)
//        val longg = longlat!!.first
//        val latt = longlat.second
//        val language = sharedPrefrencesManager.getString(SharedKey.LANGUAGE.name, "default")



        binding.imgSearchIcon.setOnClickListener {
            val searchQuery = binding.etSearchMap.text.toString()
            if (searchQuery.isNotEmpty()) {
                searchLocation(searchQuery)
            }
        }
        binding.btnSelectOrAddOnMap.setOnClickListener {
//            binding.btnSelectOrAddOnMap.isEnabled
//                sharedPrefrencesManager.saveLocationFromMap(SharedKey.GPS.name, latLng.longitude,latLng.latitude)
//                favoriteViewModel.addToFavorites()
//                favoriteViewModel.removeFromFavorites()


//            Log.i("==latttt longggg===", "" + longg + latt)
            if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "fav") {

                val longlat = sharedPrefrencesManager.getLocationFromMap(SharedKey.FAV.name)
                val longg = longlat!!.first
                val latt = longlat.second
                val language = sharedPrefrencesManager.getString(SharedKey.LANGUAGE.name, "default")


                lifecycleScope.launch(Dispatchers.Main) {
                    homeViewModel.getFiveDaysWeather(latt, longg, language)
                    homeViewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                        when (weatherResponse) {
                            is ApiState.Suceess -> {
                                Log.i("========Su", "Suceeeee: ")
                                CoroutineScope(Dispatchers.IO).launch {
//                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
                                    favoriteViewModel.addToFavorites(
                                        weatherResponse.data,
                                        weatherResponse.data.city.coord.lon,
                                        weatherResponse.data.city.coord.lat
                                    )
                                    Log.i("===db add", "onViewCreated: ")
                                }

                            }

                            is ApiState.Loading -> {
                                Log.i("======log", "loding: ")


                            }

                            else -> {
                                Log.i("========errorrr", "rrrrr: ")

                            }
                        }
                    }


                }

                replaceFragments(FavoriteFragment())

                Toast.makeText(requireContext(), "added", Toast.LENGTH_SHORT).show()

            }

            else  if (sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name, "") == "home"){

//            val ll = sharedPrefrencesManager.getLocationToHOme(SharedKey.GPS.name)
                val ll = sharedPrefrencesManager.getLocationToHOme(SharedKey.Home.name)
                val hh = ll!!.first
                val lakktt = ll.second
                val language = sharedPrefrencesManager.getString(SharedKey.LANGUAGE.name, "default")



                sharedPrefrencesManager.saveLocationToHOme(SharedKey.Home.name, hh,lakktt)
                replaceFragments(HomeFragment())



            }
        }

        binding.imgClearSearch.setOnClickListener {
           binding.etSearchMap.text.clear()
            googleMap.clear()

        //            searchLocation(emptyQuery.toString())
        }
    }

    private fun searchLocation(locationName: String) {
        val addresses = geocoder.getFromLocationName(locationName, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            val latLng = LatLng(address.latitude, address.longitude)
            moveCameraToLocation(latLng)
            addMarkerToMap(latLng, locationName)
        } else {
            Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveCameraToLocation(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
    }

    private fun addMarkerToMap(latLng: LatLng, title: String) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        val locationFromMark = getAddressFromLatLng(latLng)
        binding.etSearchMap.setText(locationFromMark)
//        sharedPrefrencesManager.savelocationChoice(SharedKey.GPS.name , "map")
        if(sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name,"")=="home"){
            Log.i("=====23low", "getAddressFromLatLng: "+sharedPrefrencesManager.getSavedMap(SharedKey.MAP.name,""))
            sharedPrefrencesManager.saveLocationToHOme(SharedKey.Home.name, latLng.longitude,latLng.latitude)
        }
        else{
            sharedPrefrencesManager.saveLocationFromMap(SharedKey.FAV.name, latLng.longitude,latLng.latitude)
        }



//        sharedPrefrencesManager.saveLocationFromMap(SharedKey.GPS.name, latLng.longitude,latLng.latitude)

    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//        sharedPrefrencesManager.saveLocationFromMap(SharedKey.GPS.name, latLng.longitude,latLng.latitude)
        return if (addresses!!.isNotEmpty()) {
            addresses[0]!!.getAddressLine(0)
        } else {
            "Unknown Location"
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
