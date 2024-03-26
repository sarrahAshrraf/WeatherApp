package com.example.weatherapppoject.alert.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapppoject.R
import com.example.weatherapppoject.alert.AlertData
import com.example.weatherapppoject.alert.NetworkListener
import com.example.weatherapppoject.alert.viewmodel.AlertViewModel
import com.example.weatherapppoject.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapppoject.database.LocalDataSourceImp
import com.example.weatherapppoject.database.LocalDataSourceInte
import com.example.weatherapppoject.databinding.AlertDialogBinding
import com.example.weatherapppoject.databinding.FragmentAlertBinding
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.network.RemoteDataSource
import com.example.weatherapppoject.network.RemoteDataSourceImp
import com.example.weatherapppoject.repository.WeatherRepositoryImpl
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
