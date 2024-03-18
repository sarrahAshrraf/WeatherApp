package com.example.weatherapppoject.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.ActivityHomeBinding
import com.example.weatherapppoject.favorite.view.FavoriteFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.utils.NetworkManager

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var networkReceiver: BroadcastReceiver
    private var isNetworkAvailable: Boolean = false
    private lateinit var  navController : NavController
    private lateinit var sharedPrefrencesManager: SharedPrefrencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefrencesManager = SharedPrefrencesManager.getInstance(this)
        sharedPrefrencesManager.savelocationChoice(SharedKey.GPS.name,"gps")

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateNetworkIndicator()
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (isNetworkAvailable) {
                when (item.itemId) {
                    R.id.home -> replaceFragments(HomeFragment())
                    R.id.notification -> replaceFragments(NotificationFragment())
                    R.id.settings -> replaceFragments(SettingsFragment())
                    R.id.favorite -> replaceFragments(FavoriteFragment())
                }
            }else {
                replaceFragments(SettingsFragment())

//                when (item.itemId) {
////                    R.id.home -> replaceFragments(BlankFragment())
//                    R.id.notification -> replaceFragments(NotificationFragment())
//                    R.id.settings -> replaceFragments(SettingsFragment())
//                    R.id.favorite -> replaceFragments(FavoriteFragment())
//                }
                    Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show()


            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(networkReceiver)
    }

    private fun updateNetworkIndicator() {
        isNetworkAvailable = NetworkManager.isNetworkAvailable(this)
        if (isNetworkAvailable) {
            binding.tvNetworkIndicator.visibility = View.GONE
            replaceFragments(SettingsFragment())
        } else {
            binding.tvNetworkIndicator.visibility = View.VISIBLE
            replaceFragments(HomeFragment())
//            replaceFragments(BlankFragment())

        }
    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}