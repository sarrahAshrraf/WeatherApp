package com.example.weatherapppoject.home.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.view.FiveDaysAdapter
import com.example.weatherapppoject.view.TodayDataAdapter
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale



class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: FiveDaysAdapter
    private lateinit var todayadapter: TodayDataAdapter
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var localDataSourceInte: LocalDataSourceInte
    lateinit var repository: WeatherRepositoryImpl
    lateinit var viewModelFactory: HomeFragmentViewModelFactory
    private val locationID = 5
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favviewModelFactory: FavoriteViewModelFactory
    private var mainLongitude:Double =0.0
    private var mainLatitude: Double =0.0
    private var language : String = "default"
    private var units : String = "metric" //"metric" celisuc
    //default -> kalvin ,,,, imperial: F

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            if (context?.let { isLocationEnabled(it) } == true) {
                getFreshLocation()
                Toast.makeText(requireContext(), "location is enabled", Toast.LENGTH_SHORT).show()
            } else {
                enableLocation()
            }
        } else {
            requestPermission()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource,localDataSourceInte)
        favviewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel = ViewModelProvider(this, favviewModelFactory).get(FavoriteViewModel::class.java)
        viewModelFactory = HomeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "ResourceType", "ShowToast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")
        binding.todayDetailsRecView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.FivedaysRec.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        Log.i("======home???", "onViewCreated: "+sharedPreferencesManager.getSavedMap(SharedKey.MAP.name,""))
        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ApiState.Suceess -> {
                        binding.scrollView2.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        binding.tvTemp.visibility = View.VISIBLE
                        binding.weatherImgView.visibility = View.VISIBLE
                        Log.i("===succe in home", "onViewCreated: ")

                        val iconId = weatherResponse.data.list[0].weather[0].icon
                        if (iconId != null) {
                            Utils.getWeatherIcon(iconId, binding.weatherImgView)
                            if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
                                binding.backGrou.setAnimation(R.raw.rainbackground)
                        }
//                            "${}°C"

                        if(sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name,"")=="ar"){
//                           if(sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name,"")=="metric"){ }
//                            else{ }
                            binding.tvTemp.text = Utils.convertToArabicNumber(weatherResponse.data.list[0].main.temp.toString())

                            binding.cloudPercent.text = Utils.convertToArabicNumber(weatherResponse.data.list[0].clouds?.all.toString()+"%")
                            binding.windPercent.text = Utils.convertToArabicNumber(weatherResponse.data.list[0].wind?.speed.toString())
                            binding.tvDayFormat.text =Utils.convertToArabicNumber( weatherResponse.data.list[0].dt_txt)
                            binding.humidityPercent.text = Utils.convertToArabicNumber(weatherResponse.data.list[0].main.humidity.toString())
                            binding.pressurePercent.text = Utils.convertToArabicNumber(weatherResponse.data.list[0].main.pressure.toString())
                            binding.tvStatus.text = weatherResponse.data.list[0].weather[0].description

                        }else {
//                            if(sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name,"")=="metric"){}
//                            else{}
                            binding.tvTemp.text = "${weatherResponse.data.list[0].main.temp}°C"
                            binding.cloudPercent.text = "${weatherResponse.data.list[0].clouds?.all.toString()}%"
                            binding.windPercent.text = weatherResponse.data.list[0].wind?.speed.toString()
                            binding.tvDayFormat.text = weatherResponse.data.list[0].dt_txt
                            binding.humidityPercent.text = weatherResponse.data.list[0].main.humidity.toString()
                            binding.pressurePercent.text = weatherResponse.data.list[0].main.pressure.toString()
                            binding.tvStatus.text = weatherResponse.data.list[0].weather[0].description

                        }
                    }

                    is ApiState.Loading -> {
                        binding.tvTemp.visibility = View.GONE
                        binding.weatherImgView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView2.visibility = View.GONE
                        Log.i("===lodaing in home", "onViewCreated: ")
                    }

                    else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView2.visibility = View.GONE

                    }
                }
            }


        }


        //todo change the view model name variabl => todaysData
        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ApiState.Suceess -> {
                        binding.todayDetailsRecView.visibility = View.VISIBLE
                        binding.todayDetailsRecView.visibility = View.VISIBLE

                        val forecastList = weatherResponse.data.list
                        val forecastItems = forecastList
                            .take(8)
                        adapter = FiveDaysAdapter(forecastItems,language)
                        binding.todayDetailsRecView.adapter = adapter
                    }

                    is ApiState.Loading -> {
                        binding.FivedaysRec.visibility = View.GONE
                        binding.todayDetailsRecView.visibility = View.GONE
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }


        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ApiState.Suceess -> {
                        binding.FivedaysRec.visibility = View.VISIBLE
                        binding.todayDetailsRecView.visibility = View.VISIBLE

                        val date = Utils.getDatefortvDate(weatherResponse.data.list[0].dt_txt)

//            // TODO Update the UI with the TODAAAY forecast data
                        val filteredList = weatherResponse.data.list.filter { forecastData ->
                            val time = forecastData.dt_txt.split(" ")[1]
                            val hour = time.split(":")[0].toInt()
                            hour == 12
                        }
                        todayadapter = TodayDataAdapter(filteredList,language)
                        binding.FivedaysRec.adapter = todayadapter
                    }

                    is ApiState.Loading -> {
                        binding.FivedaysRec.visibility = View.GONE
                        binding.todayDetailsRecView.visibility = View.GONE
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    }
                }
            }


        }
    }



    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        val locationCallback = object : LocationCallback() {
            @SuppressLint("SuspiciousIndentation")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location: android.location.Location? = locationResult.lastLocation
                language = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")
                units= sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name, "")
                if (location != null && sharedPreferencesManager.getlocationChoice(SharedKey.GPS.name, "") == "gps") {

                    mainLongitude = location.longitude
                    mainLatitude = location.latitude

                    viewModel.getFiveDaysWeather(mainLatitude,mainLongitude ,language,units)
                    viewModel.getAlertsInfo(mainLatitude,mainLongitude)

                    displayAddress(mainLatitude, mainLongitude)
                    displayfullAddress(mainLatitude, mainLongitude)
                    sharedPreferencesManager.saveLocationToAlert(SharedKey.ALERT.name, mainLongitude,mainLatitude)

                }
                else {
                    if (sharedPreferencesManager.getlocationChoice( SharedKey.GPS.name,"" ) == "map"
                        && sharedPreferencesManager.getSavedMap(SharedKey.MAP.name,"")=="home") {
                        val longlat =
                            sharedPreferencesManager.getLocationToHOme(SharedKey.Home.name)
                        mainLongitude = longlat!!.first
                        mainLatitude = longlat.second

                        viewModel.getFiveDaysWeather(mainLatitude, mainLongitude,language,"imperial")
                        viewModel.getAlertsInfo(mainLatitude,mainLongitude)
                        displayAddress(mainLatitude, mainLongitude)
                        displayfullAddress(mainLatitude, mainLongitude)
                        sharedPreferencesManager.saveLocationToAlert(SharedKey.ALERT.name, mainLongitude,mainLatitude)


                    }
                    else {
                        Toast.makeText(requireContext(),"No"+sharedPreferencesManager.getSavedMap(SharedKey.MAP.name,""),Toast.LENGTH_SHORT).show()


                    }
                }

                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }



        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun displayAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.countryName
                binding.city.text = city

            }
        }
    }

    fun displayfullAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        Log.i("=====long lat", "long lat of alex: " +longitude +"  "+latitude)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val locality = address.adminArea // Extract the city from the address
               if (locality.isNotEmpty()) {
                   binding.tvFullocation.text = locality!!.toString() //TODO null check!!!
               }
            }
        }
    }


    private fun enableLocation() {
        Toast.makeText(requireContext(), "Turn On location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            locationID
        )

    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

}
