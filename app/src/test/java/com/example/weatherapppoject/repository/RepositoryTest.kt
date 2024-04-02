package com.example.weatherapppoject.repository

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.data.FakeLocalDataCourceImp
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import com.example.weatherapppoject.network.FakeApiService
import com.example.weatherapppoject.network.FakeRemoteDataCource
import com.google.common.truth.ExpectFailure.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    val lang = "en"
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
    val favorite1 = WeatherResponse(
        9,
        city,
        123.456,
        78.90,
        5,
        1,
        0,
        "200",
        forecastList,
        0
    )

    val favorite2 = WeatherResponse(
        23,
        city,
        12.456,
        12.90,
        5,
        1,
        0,
        "200",
        forecastList,
        0
    )

    val home = WeatherResponse(
        9,
        city,
        123.456,
        78.90,
        5,
        0,
        1,
        "200",
        forecastList,
        0
    )
    val alert1 = AlertData(
        1,
        "9:00 AM",
        "2024-03-28",
        "6:00 PM",
        "2024-03-29",
        1658828400000L,
        17653L,
        1658886000000L,
        17654L,
        123456,
        "123.456",
        "78.90"
    )

    val alert2 = AlertData(
        2,
        "11:00 AM",
        "2024-03-20",
        "2:00 PM",
        "2024-03-29",
        1658828400000L,
        17653L,
        1658886000000L,
        17654L,
        123456,
        "33.456",
        "18.90"
    )

    private val remote = WeatherResponse(

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
    private val localHome = listOf(home).toMutableList()
    private val localFavorite = listOf(favorite1,favorite2).toMutableList()
    private val localAlert = listOf(alert1,alert2).toMutableList()


    private lateinit var remoteSource : FakeApiService
    private lateinit var localSource : FakeLocalDataCourceImp
    private lateinit var repository: WeatherRepositoryImpl


    @Before
    fun getSetup(){
        remoteSource = FakeApiService(remote)
        localSource = FakeLocalDataCourceImp(localHome,localFavorite,localAlert)
        repository = WeatherRepositoryImpl.getInstance(remoteSource,localSource)
    }


    @Test
    fun getWeatherData_fromRemote_returnWeatherDataLatitude() = runTest{
        var response  = 0.0
        repository.getFiveDaysWeather(0.0,0.0,"","","").collect{
            response = it.latitude
        }
        Assert.assertThat(response, IsEqual(remote.latitude))
    }


    @Test
    fun getFavorites_insertFavItem_returnFavoriteDataLatitude() = runTest{
        var favorite = 0.0
        repository.getFavoriteData().collect{
            favorite = it[0].latitude
        }
        Assert.assertThat(favorite, IsEqual(localFavorite[0].latitude))
    }

    @Test
    fun getWeatherData_fromLocal_returnWeatherDataLatitude() = runTest{
        var home = 0.0
        repository.getFiveDaysWeather(0.0,0.0,"","","").collect{
            home = it.latitude
        }
        Assert.assertThat(home, IsEqual(localHome[0].latitude))
    }

    @Test
    fun insertWeatherData_insertHomeData_returnWeatherDataId() = runTest{

    val home =WeatherResponse(
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
        repository.insertHomeData(home,123.456,78.90)
        var home2 = 0
        repository.getFavCityInfoHome().collect{
            home2 = it.id
        }
        Assert.assertThat(home2, IsEqual(9))
    }

    @Test
    fun insertWeatherData_insertFavortieData_returnWeatherDataId() = runTest{
        val favorite = WeatherResponse(
            6,
            city,
            123.456,
            78.90,
            5,
            1,
            0,
            "200",
            forecastList,
            0

        )
        repository.insertfavIntoDB(favorite,0.0,0.0)

        var favorite1 = 0
        repository.getFavoriteData().collect{
            favorite1 = it[it.size-1].id
        }
        Assert.assertThat(favorite1, IsEqual(6))
    }

    @Test
    fun deleteFavWeatherData_insertTwoFavData_returnWeatherDataIdOfTheNotDeleted() = runTest{
        val favorite1 = WeatherResponse(
            99,
            city,
            123.456,
            78.90,
            5,
            1,
            0,
            "200",
            forecastList,
            0
        )

        val favorite2 = WeatherResponse(
            12,
            city,
            123.456,
            78.90,
            5,
            1,
            0,
            "200",
            forecastList,
            0
        )
        repository.insertfavIntoDB(favorite1,0.0,0.0)
        repository.insertfavIntoDB(favorite2,0.0,0.0)
        repository.deleteFromFav(favorite1)
        var favorite = 0
        repository.getFavoriteData().collect{
            favorite = it[0].id
        }
        Assert.assertThat(favorite, IsEqual(12))
    }

}
