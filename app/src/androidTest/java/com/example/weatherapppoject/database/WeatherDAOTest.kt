package com.example.weatherapppoject.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.forecastmodel.City
import com.example.weatherapppoject.forecastmodel.Clouds
import com.example.weatherapppoject.forecastmodel.Coord
import com.example.weatherapppoject.forecastmodel.ForeCastData
import com.example.weatherapppoject.forecastmodel.Main
import com.example.weatherapppoject.forecastmodel.Weather
import com.example.weatherapppoject.forecastmodel.WeatherResponse
import com.example.weatherapppoject.forecastmodel.Wind
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDAOTest {

    private lateinit var db : AppDB
    private lateinit var dao: WeatherDao

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDB::class.java
        ).build()
        dao = db.getWeatherDAO()
    }

    @After
    fun deleteDatabase()  = db.close()

//passed
    @Test
    fun insertWeatherData() = runBlockingTest {
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
        val weatherResponse = WeatherResponse(
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


        dao.insertWeatherData(weatherResponse)

        val storedData = dao.getWeatherData().first()

        assertNotNull(storedData)
        assertEquals(weatherResponse, storedData)
    }

    @Test
    fun getWeatherData() = runBlockingTest {
        val weatherData = createNotFavData()

        dao.insertWeatherData(weatherData)

        val storedData = dao.getWeatherData().first()

        assertNotNull(storedData)
        assertEquals(weatherData, storedData)
    }
    //passed
    @Test
    fun insertFav() = runBlockingTest {
        val favorite = createFavData()

        dao.insertFav(favorite)

        val storedData = dao.getFav().first()

        assertNotNull(storedData)
        assertEquals(listOf(favorite), storedData)
    }
//passed
    @Test
    fun deleteFav() = runBlockingTest {
        val favorite = createFavData()

        dao.insertFav(favorite)

        dao.deleteFav(favorite)

        val storedData = dao.getFav().first()

        assertTrue(storedData.isEmpty())


}

    @Test
    fun insertAlert() = runBlockingTest {
        val alert =  AlertData(
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

        dao.insertAlert(alert)

        val storedData = dao.getAlertsData().first()

        assertNotNull(storedData)
        assertEquals(listOf(alert), storedData)
    }

    fun createFavData() : WeatherResponse {
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
        val weatherResponse = WeatherResponse(
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
        return weatherResponse
    }
    fun createNotFavData() :WeatherResponse{
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
        val weatherResponse = WeatherResponse(
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
        return weatherResponse


    }
}