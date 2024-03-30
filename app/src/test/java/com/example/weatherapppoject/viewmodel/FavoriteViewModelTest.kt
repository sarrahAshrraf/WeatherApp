package com.example.weatherapppoject.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapppoject.MainRule
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.alert.viewmodel.AlertViewModel
import com.example.weatherapppoject.getOrAwaitValue
import com.example.weatherapppoject.repository.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val main = MainRule()

    private lateinit var alertViewModel: AlertViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun getSetup() {
        repository = FakeRepository()
        alertViewModel = AlertViewModel(repository)
    }


    @Test
    fun getAlerts(): Unit = runBlocking {
        val alertData = AlertData(
            5,
            "1:00 AM",
            "2023-03-28",
            "6:00 AM",
            "2024-03-19",
            1658828400000L,
            17653L,
            1658886000000L,
            17654L,
            123456,
            "39.456",
            "31.90"
        )
        val expectedAlertData = listOf(alertData)

        alertViewModel.insertIntoAlert(alertData)
        alertViewModel.getAlertData()
        Assert.assertThat(
            alertViewModel.alertData.getOrAwaitValue(), CoreMatchers.equalTo(expectedAlertData)
        )

    }

}