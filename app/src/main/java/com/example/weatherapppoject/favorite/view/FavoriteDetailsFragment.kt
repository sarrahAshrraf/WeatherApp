package com.example.weatherapppoject.favorite.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapppoject.R
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.FragmentFavoriteDetailsBinding
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModel
import com.example.weatherapppoject.favorite.viewmodel.FavoriteViewModelFactory
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.ForeCastApiState
import com.example.weatherapppoject.utils.DBState
import com.example.weatherapppoject.utils.Utils
import com.example.weatherapppoject.home.view.TodayAdapter
import com.example.weatherapppoject.home.view.WeekAdapter
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteDetailsBinding
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSourceInte: LocalDataSourceInte
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var repository: WeatherRepositoryImpl
    private lateinit var viewModelFactory: FavoriteViewModelFactory
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager
    private lateinit var adapter: TodayAdapter
    private lateinit var todayadapter: WeekAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    lateinit var HomeviewModelFactory: HomeFragmentViewModelFactory
    private var language : String ="default"
    private var units : String ="default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        remoteDataSource = RemoteDataSourceImp()
        localDataSourceInte = LocalDataSourceImp(requireContext())
        repository = WeatherRepositoryImpl.getInstance(remoteDataSource, localDataSourceInte)

        viewModelFactory = FavoriteViewModelFactory(repository)
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)

        HomeviewModelFactory = HomeFragmentViewModelFactory(repository)
        viewModel = ViewModelProvider(this, HomeviewModelFactory).get(HomeFragmentViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        language = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default")
        units= sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name,"")
        val longLatArray = arguments?.getDoubleArray("longlat")
        if (longLatArray != null && longLatArray.size == 2) {
            val longitude = longLatArray[0]
            val latitude = longLatArray[1]
             viewModel.getFiveDaysWeather(latitude,longitude,language,units)
            lifecycleScope.launch(Dispatchers.Main) {
            viewModel.weatherData.collectLatest { weatherResponse ->
               when (weatherResponse) {
               is ForeCastApiState.Suceess -> {
            binding.todayDetailsRecView.visibility = View.VISIBLE
            binding.todayDetailsRecView.visibility = View.VISIBLE
            binding.tvTodayRecTxt.visibility = View.VISIBLE
            binding.textDays.visibility = View.VISIBLE
           val forecastList = weatherResponse.data.list
            val forecastItems = forecastList
            .take(8)// Display only the first 5 forecast items
            adapter = TodayAdapter(forecastItems,language,units)
            binding.todayDetailsRecView.adapter = adapter
        }

        is ForeCastApiState.Loading -> {
        lifecycleScope.launch(Dispatchers.Main) {
        favoriteViewModel.showWeatherDetails(longitude, latitude)
        favoriteViewModel.favorite.collect { state ->
            when (state) {
             is DBState.Loading -> {}
             is DBState.OneCitySucess -> {
                 binding.apply {
                 tvTodayRecTxt.visibility = View.GONE
                textDays.visibility = View.GONE
                city.text = state.cityData.city.name
                tvTemp.text = state.cityData.list[0].main.temp.toString()
                humidityPercent.text = state.cityData.list[0].main.humidity.toString()
                pressurePercent.text = state.cityData.list[0].main.pressure.toString()
                cloudPercent.text = state.cityData.list[0].clouds.all.toString()
                tvStatus.text = state.cityData.list[0].weather[0].description


            }
        }

        else -> {
            Toast.makeText(requireContext(), "Loading..",Toast.LENGTH_SHORT).show()
        }
    }


        }
        }
        }
        else -> {}
}
}


}

lifecycleScope.launch(Dispatchers.Main) {

    viewModel.weatherData.collectLatest { weatherResponse ->
        when (weatherResponse) {
    is ForeCastApiState.Suceess -> {

        CoroutineScope(Dispatchers.IO).launch {
            favoriteViewModel.removeFromFavorites(
                weatherResponse.data
            )
        }
        binding.FivedaysRec.visibility = View.VISIBLE
        binding.todayDetailsRecView.visibility = View.VISIBLE

        val date = Utils.getDate(weatherResponse.data.list[0].dt_txt)

        val filteredList = weatherResponse.data.list.filter { forecastData ->
            val time = forecastData.dt_txt.split(" ")[1]
            val hour = time.split(":")[0].toInt()
            hour == 12
        }
        todayadapter = WeekAdapter(filteredList,language,units)
        binding.FivedaysRec.adapter = todayadapter
    }

    is ForeCastApiState.Loading -> {
        lifecycleScope.launch(Dispatchers.Main) {
            favoriteViewModel.showWeatherDetails(longitude, latitude)

            favoriteViewModel.favorite.collect { state ->
                when (state) {
                    is DBState.Loading -> {
                        Log.i("====loading details name", "fav details fragment: ")

                    }

        is DBState.OneCitySucess -> {
            binding.apply {
                city.text = favoriteViewModel.getAddressFromCoordinates(state.cityData.latitude,state.cityData.longitude,requireContext())
                tvTemp.text = state.cityData.list[0].main.temp.toString()
                humidityPercent.text = state.cityData.list[0].main.humidity.toString()
                pressurePercent.text = state.cityData.list[0].main.pressure.toString()
                cloudPercent.text = state.cityData.list[0].clouds.all.toString() +"%"
                tvStatus.text = state.cityData.list[0].weather[0].description
                tvDayFormat.text = "Last Updated At \n ${Utils.getDate(state.cityData.list[0].dt_txt)}"
                val iconId = state.cityData.list[0].weather[0].icon
                if (iconId != null) {
                    Utils.getWeatherIcon(iconId, binding.weatherImgView)
                    if (iconId == "09d" || iconId == "09n" || iconId == "10d" || iconId == "10n")
                        binding.backGrou.setAnimation(R.raw.rainbackground)
                }
            }
        }
    else -> { binding.cloudPercent.visibility = View.GONE }

        }

          }
        }
        }

        else -> { }
    }
}

    }

        }

            }


             }
