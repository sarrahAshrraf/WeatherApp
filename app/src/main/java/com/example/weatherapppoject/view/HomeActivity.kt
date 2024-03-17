package com.example.weatherapppoject.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.ActivityHomeBinding
import com.example.weatherapppoject.utils.NetworkManager

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var networkReceiver: BroadcastReceiver
    private var isNetworkAvailable: Boolean = false
    private lateinit var  navController : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                updateNetworkIndicator()
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (isNetworkAvailable) {
                when (item.itemId) {
                    R.id.home -> replaceFragments(MapsFragment())
                    R.id.notification -> replaceFragments(NotificationFragment())
                    R.id.settings -> replaceFragments(SettingsFragment())
                    R.id.favorite -> replaceFragments(FavoriteFragment())
                }
            }else {
                replaceFragments(MapsFragment())

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
            replaceFragments(MapsFragment())
        } else {
            binding.tvNetworkIndicator.visibility = View.VISIBLE
            replaceFragments(MapsFragment())
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