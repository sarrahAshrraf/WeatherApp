package com.example.weatherapppoject.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.health.connect.datatypes.ExerciseRoute.Location
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.http.HttpException
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
import androidx.annotation.RequiresExtension
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.AppDB
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.databinding.FragmentHomeBinding
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.network.RetrofitInstance
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ApiState
import com.example.weatherapppoject.utils.OneCallState
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.viewmodel.HomeFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.format.TextStyle
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

                if (location != null && sharedPreferencesManager.getlocationChoice(
                        SharedKey.GPS.name,
                        ""
                    ) == "gps"
                ) {
                    var long = location.longitude
                    var lat = location.latitude
                    viewModel.getFiveDaysWeather(long, lat)
                    viewModel.getFiveDaysWeather(long, lat)
                    viewModel.getAlertsInfo(long,lat)

                    displayAddress(lat, long)
                    displayfullAddress(lat, long)
                }
                else {
                    if (sharedPreferencesManager.getlocationChoice(
                            SharedKey.GPS.name,
                            ""
                        ) == "map"
                    ) {
                        val longlat =
                            sharedPreferencesManager.getLocationFromMap(SharedKey.GPS.name)
                        val longg = longlat!!.first
                        val latt = longlat.second

                        viewModel.getFiveDaysWeather(latt, longg)
                        viewModel.getAlertsInfo(latt,longg)
//                        viewModel.getFiveDaysWeather(latt, longg)
                        displayAddress(latt, longg)
                        displayfullAddress(latt, longg)

                    }
//                    else {
//                        Toast.makeText(requireContext(),"No location",Toast.LENGTH_SHORT).show()
//
//
//                    }
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
    //use the geocoder

    fun displayAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                val city = address.countryName // Extract the city from the address
                binding.city.text = city

            }
        }
    }

    fun displayfullAddress(latitude: Double, longitude: Double) {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
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


//        if (addresses != null) {
//            if (addresses.isNotEmpty()) {
//                val address = addresses[0]
//                val fullAddress = address.getAddressLine(0)
//                binding.city.text = fullAddress
//            }
//        }
//    }

    private fun enableLocation() {
        Toast.makeText(requireContext(), "Turn On location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        remoteDataSource = RemoteDataSourceImp()
//why context??????????????
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

    @SuppressLint("SetTextI18n", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.todayDetailsRecView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.FivedaysRec.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//        lifecycleScope.launch(Dispatchers.Main) {
//            viewModel.currentWeather.collectLatest { weatherList ->
//                when (weatherList) {
//                    is ApiState.Suceess -> {
//                        binding.tvTemp.text = "${weatherList.data.main?.temp}°C"
//                        binding.cloudPercent.text = "${weatherList.data.clouds?.all.toString()}%"
//                        binding.windPercent.text = weatherList.data.wind?.speed.toString()
////                        binding.tvDayFormat.text = weatherList.data.dtTxt.toString()
//                        binding.humidityPercent.text = weatherList.data.main?.humidity.toString()
//                        binding.pressurePercent.text = weatherList.data.main?.pressure.toString()
//                        binding.tvStatus.text = weatherList.data.weather[0].description
//
//                        val iconId = weatherList.data.weather[0].icon
//                        if (iconId != null) {
//                            Utils.getWeatherIcon(iconId, binding.weatherImgView)
//                            if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
//                                binding.backGrou.setAnimation(R.raw.rainbackground)
//                        }
//
//                        binding.FivedaysRec.visibility = View.VISIBLE
//                        binding.todayDetailsRecView.visibility = View.VISIBLE
//                    }
//                    is ApiState.Loading -> {
//                        binding.FivedaysRec.visibility = View.GONE
//                        binding.todayDetailsRecView.visibility = View.GONE
//                    }
//                    else -> {
//                        binding.tvTemp.visibility = View.GONE
//                        Log.i("=====error api", "Error in loading api ")
//                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }


//todo move to alert fragment
        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.alertsData.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is OneCallState.Suceess -> {
                        Log.i("==home fragment alert", ""+weatherResponse.data.alerts)

                    }

                    is OneCallState.Loading -> {

                        Log.i("===lodaing in Alerts", "onViewCreated: ")

                    }

                    else -> {

                        Log.i("===error in Alerts", "onViewCreated: ")

                    }
                }
            }


        }







        lifecycleScope.launch(Dispatchers.Main) {

            viewModel.fiveDaysWeather.collectLatest { weatherResponse ->
                when (weatherResponse) {
                    is ApiState.Suceess -> {
                        binding.scrollView2.visibility = View.VISIBLE
//                        binding.backGrou.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        binding.tvTemp.visibility = View.VISIBLE
                        binding.weatherImgView.visibility = View.VISIBLE
                        Log.i("===succe in home", "onViewCreated: ")

                        binding.tvTemp.text = "${weatherResponse.data.list[0].main.temp}°C"
                        binding.cloudPercent.text = "${weatherResponse.data.list[0].clouds?.all.toString()}%"
                        binding.windPercent.text = weatherResponse.data.list[0].wind?.speed.toString()
                        binding.tvDayFormat.text = weatherResponse.data.list[0].dt_txt
                        binding.humidityPercent.text = weatherResponse.data.list[0].main.humidity.toString()
                        binding.pressurePercent.text = weatherResponse.data.list[0].main.pressure.toString()
                        binding.tvStatus.text = weatherResponse.data.list[0].weather[0].description

//                        binding.addbtn.setOnClickListener {
////                            val db = Room.databaseBuilder(requireContext(), AppDB::class.java, "rr").build()
//                            CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().setAsFavorite(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                            favoriteViewModel.addToFavorites(weatherResponse.data,weatherResponse.data.city.coord.lon,weatherResponse.data.city.coord.lat)
//                                Log.i("===db scope", "onViewCreated: ")
//                        }
//                        }


//                        binding.deletebtn.setOnClickListener {
//                            val db = Room.databaseBuilder(requireContext(), AppDB::class.java, "rr").build()
//                            CoroutineScope(Dispatchers.IO).launch {
////                                db.getWeatherDAO().deleteFavByIsFav()
//                                Log.i("d======eeee","onViewCreated")
//                                db.getWeatherDAO().deleteFavByLonLat(weatherResponse.data.longitude,weatherResponse.data.latitude)
//
//                            }
//                        }

                        val iconId = weatherResponse.data.list[0].weather[0].icon
                        if (iconId != null) {
                            Utils.getWeatherIcon(iconId, binding.weatherImgView)
                            if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
                                binding.backGrou.setAnimation(R.raw.rainbackground)
                        }

                    }

                    is ApiState.Loading -> {
                        binding.tvTemp.visibility = View.GONE
                        binding.weatherImgView.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                        binding.scrollView2.visibility = View.GONE

//                        binding.backGrou.visibility = View.VISIBLE
//                        binding.backGrou.setAnimation(R.raw.clouds)

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
                            .take(8)// Display only the first 5 forecast items
                        adapter = FiveDaysAdapter(forecastItems)
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
                        todayadapter = TodayDataAdapter(filteredList)
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


            ///TODO the rest of the week !!! Only item per week is enough

//             viewModel.fiveDaysWeather.observe(viewLifecycleOwner) {
//                 weatherResponse ->
//                 val date = Utils.getDatefortvDate(weatherResponse.list[0].dt_txt)
//                 binding.tvDayFormat.text = date.toString()
//            // TODO Update the UI with the TODAAAY forecast data
//                 val filteredList = weatherResponse.list.filter { forecastData ->
//                val time = forecastData.dt_txt.split(" ")[1]
//                val hour = time.split(":")[0].toInt()
//                hour == 12
//
//            }

//            todayadapter = TodayDataAdapter(filteredList)
//            binding.FivedaysRec.adapter = todayadapter


//        viewModel.getCurrentWeather()
//        viewModel.getFiveDaysWeather()
        }
    }
}








//
//class HomeFragment : Fragment() {
//    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
//    lateinit var binding: FragmentHomeBinding
//    private lateinit var viewModel: HomeFragmentViewModel
//
//    lateinit var recyclerView: RecyclerView
////    lateinit var adapter: FiveDaysAdapter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
//    val remoteDataSource = YourRemoteDataSourceImplementation() // Implement the RemoteDataSource interface
//    val repository = WeatherRepositoryImpl(remoteDataSource)
//    val viewModelFactory = HomeFragmentViewModelFactory(repository)
//    viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
//

//    }
//


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val locale = Locale(sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "default"))
//        Locale.setDefault(locale)
//        val resources = requireContext().resources
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//        val context= ContextUtils.wrapContext(requireContext(), locale)
//
////        binding.swipeRefreshLayout.setOnRefreshListener {
////            findNavController(view).navigate(R.id.home)
////        }
//
//
//        getCurrentWeather()
//
//        getForeCast()
//        ///recView
////        adapter = FiveDaysAdapter()
//        binding.todayDetailsRecView.apply {
//            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
////            adapter = this.adapter
//        }
//
//
//
//        binding.city.setText(context.getString(R.string.city))
//        binding.tvWind.setText(context.getString(R.string.winds))
//        binding.textDays.setText(context.getString(R.string.language))
//        binding.tvPressure.setText(context.getString(R.string.pressure))
//        binding.textDays.setText(context.getString(R.string.next5days))
//        binding.tvTemp.setText(context.getString(R.string.weathertemp))
//        binding.tvHumidity.setText(context.getString(R.string.humidity))
//        binding.tvclouds.setText(context.getString(R.string.clouds))
////        binding.tvDayFormat.setText(context.getString(R.string.dayformat))
//        binding.tvStatus.setText(context.getString(R.string.weatherstatus))
//    }
//
//
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//private fun getForeCast() {
//    GlobalScope.launch(Dispatchers.IO) {
//        val response = try {
//            RetrofitInstance.wetherAPi.getForeCast("japan", "metric", Utils.APIKEY,"ar")
//        } catch (e: IOException) {
////            Toast.makeText(requireContext(), "error" + e, Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error" + e)
//            return@launch
//        } catch (e: HttpException) {
////            Toast.makeText(requireContext(), "" + e, Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error" + e)
//
//            return@launch
//
//
//        }
//
//        if (response.isSuccessful && response.body() != null) {
//
//            withContext(Dispatchers.Main) {
//                val data = response.body()!!
//                var forecastArray = arrayListOf<ForeCastData>()
//                forecastArray = data.list as ArrayList<ForeCastData>
//                val adapter = FiveDaysAdapter(forecastArray)
//                binding.todayDetailsRecView.adapter = adapter
//            }
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//private fun getCurrentWeather(){
//    GlobalScope.launch (Dispatchers.IO){
//        val response = try {
//            RetrofitInstance.wetherAPi.getCureentWeather("egypt","metric", Utils.APIKEY)
//        } catch (e: IOException){
////        Toast.makeText(requireContext(),"error"+e,Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error"+e)
//        return@launch
//    } catch (e :HttpException){
////            Toast.makeText(requireContext(),""+e,Toast.LENGTH_SHORT).show()
//            Log.i("============", "Error"+e)
//
//            return@launch
//
//        }
//
//    if(response.isSuccessful && response.body()!=null){
//        Log.i("===========", "city: "+response.body()!!.clouds.toString())
//        Log.i("===========", "city: "+response.body()!!.main?.temp)
//
//        withContext(Dispatchers.Main){
//           val data = response.body()
////            binding.tvDayFormat.text= SimpleDateFormat(
////                "hh:mm a",
////                Locale.ENGLISH
////            ).format(data.sys.)
//           binding.apply {
//               //TODO check if the setting is ar and convert the numbers
//               //TODO add the lang property in the api call like the forecast in APIservice
//               tvTemp.text =
//                   Utils.convertToArabicNumber( response.body()!!.main?.temp.toString())
////                   response.body()!!.main?.temp.
////               toString()
//               cloudPercent.text= response.body()!!.clouds!!.all.toString()
//               windPercent.text = response.body()!!.wind!!.speed.toString()
//               tvDayFormat.text = response.body()!!.dtTxt
//               humidityPercent.text = response.body()!!.main?.humidity.toString()
//               pressurePercent.text = response.body()!!.main?.pressure.toString()
//               tvStatus.text = response.body()!!.weather[0].description
//               tvDayFormat.text = response.body()!!.dtTxt?.let { Utils.getDateAndTime(it) }//////NULL
//
//
//
//
//           }
///*
// icons : day d + night n
// 01 - sunny
// 02 - sun +cloud
// 03 - one cloud
// 04 - clouds
// 09 - rains
//*/
//
//            val iconId = data!!.weather[0].icon
//            val imgURL = "https://openweathermap.org/img/w/$iconId.png"
//
//            Log.i("===========icin", "getCurrentWeather: "+data.weather[0].icon)
//
//            when (iconId) {
//                "01d" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.ddsunny)
//                        .into(binding.weatherImgView)
//                }
//                "01n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.sunny)
//                        .into(binding.weatherImgView)
//                }
//                "02d" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.partlycloudy)
//                        .into(binding.weatherImgView)
//                }
//                "02n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.cloudynight)
//                        .into(binding.weatherImgView)
//                }
//                "03d", "03n", "04d", "04n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.cloudy)
//                        .into(binding.weatherImgView)
//                }
//                "09d", "09n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.ddrainy)
//                        .into(binding.weatherImgView)
//                }
//                "10d", "10n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.rainy)
//                        .into(binding.weatherImgView)
//                }
//                "11d", "11n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.thunderstorm)
//                        .into(binding.weatherImgView)
//                }
//                "13d", "13n" -> {
//                    Glide.with(requireContext())
//                        .load(R.drawable.snow)
//                        .into(binding.weatherImgView)
//                }
//                "50d", "50n" -> {
//                    Glide.with(requireContext())
////                        .load(R.drawable.mist)
////                        .into(binding.weatherImgView)
//                }
//                else -> {
//                    Glide.with(requireContext())
//                        .load(imgURL)
//                        .into(binding.weatherImgView)
//                }
//            }
////
////            if (iconId =="04n"){
////                val imgURL = "https://openweathermap.org/img/w/$iconId.png"
////                Glide.with(requireContext())
////                    .load(R.drawable.cloudy)
////                    .into(binding.weatherImgView)
////            }
//
//        }
//    }
//    }
//}
//}
