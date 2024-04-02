package com.example.weatherapppoject.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.MainRule
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import com.example.weatherapppoject.home.viewmodel.HomeFragmentViewModel
import com.example.weatherapppoject.repository.FakeRepository
import com.example.weatherapppoject.utils.ForeCastApiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeViewModelAPITest {


    @get:Rule
    val main = MainRule()

    private lateinit var homeViewModel: HomeFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun getSetup() {
        repository = FakeRepository()
        homeViewModel = HomeFragmentViewModel(repository)
    }




    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetFiveDaysWeather() = runTest {
        val latitude = 78.9
        val longitude = 123.456
        val lang = "en"
        val units = "metric"
        val city = City(Coord(12.345, 67.890), "Country", 123, "City", 100000, 123456, 789012, 3600)
        val forecastList = mutableListOf(
            ForeCastData(
                Clouds(0),
                1658886000,
                "2024-03-26 12:00:00",
                Main(25.0, 15, 20, 1902, 30, 22.0, 90.0, 100.0, 10.0),
                10000,
                mutableListOf(Weather("Clear", "01d", 800, "Clear Sky")),
                Wind(10, 2.5, 3.4)
            )
        )
        val fakeWeatherData = WeatherResponse(
            9,
            city,
            123.456,
            78.90,
            5,
            0,
            0,
            "200",
            forecastList,
            0
        )
        homeViewModel.getFiveDaysWeather(latitude,longitude,lang,units)

        val observedState = mutableListOf<ForeCastApiState>()
        val job = launch {
            homeViewModel.weatherData.collect {
                observedState.add(it)
            }
        }

        delay(3000)
        job.cancel()

        Assert.assertTrue(observedState.last() is ForeCastApiState.Suceess)
        Assert.assertEquals(fakeWeatherData, (observedState.last() as ForeCastApiState.Suceess).data)


    }



}