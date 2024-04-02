package com.example.weatherapppoject.settings

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.FragmentSettingsBinding
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.view.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferencesManager: SharedPrefrencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())
        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")

          }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.mapBtn -> {
                    if (isChecked) {
                        binding.mapBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.gpsBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.savelocationChoice(SharedKey.GPS.name , "map")
                        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")
                        sharedPreferencesManager.saveLanguage(SharedKey.GPS.name, "map")
                        if(checkForInternet(requireContext())){
                            replaceFragments(MapsFragment())
                        }
                        else {
                            Toast.makeText(requireContext(),getString(R.string.networkstatus),Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        binding.mapBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
                R.id.gpsBtn -> {
                    if (isChecked) {
                        binding.gpsBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.mapBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.savelocationChoice(SharedKey.GPS.name , "gps")
                        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")



                    } else {
                        binding.gpsBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
            }
        }


        binding.langueRadioGrop.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.arRdiobtn -> {
                    if (isChecked) {
                        binding.arRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.enRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        setLocale("ar")
                        sharedPreferencesManager.saveLanguage(SharedKey.LANGUAGE.name, "ar")

                    } else {
                        binding.arRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
                R.id.enRdiobtn -> {
                    if (isChecked) {
                        binding.enRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.arRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.saveLanguage(SharedKey.LANGUAGE.name, "en")
                        setLocale("en")


                    } else {
                        binding.enRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
            }
        }





        binding.unitsRadioGrop.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.matricRdiobtn -> {
                    if (isChecked) {
                        binding.matricRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.impRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)

                        sharedPreferencesManager.saveUnitsType(SharedKey.UNITS.name, "metric") //m/s

                    } else {
                        binding.matricRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
                R.id.impRdiobtn -> {
                    if (isChecked) {
                        binding.impRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.matricRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.saveUnitsType(SharedKey.UNITS.name, "imperial") // miles/hrs



                    } else {
                        binding.impRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
            }
        }




        binding.tempUnitsRadioGrop.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.CRdiobtn -> {
                    if (isChecked) {
                        binding.CRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.KRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        binding.FRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)

                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "metric")

                    } else {
                        binding.CRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
                R.id.KRdiobtn -> {
                    if (isChecked) {

                        binding.KRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.CRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        binding.FRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "standard")




                    } else {
                        binding.KRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }


                R.id.FRdiobtn -> {
                    if (isChecked) {

                        binding.FRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.CRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        binding.KRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "imperial")




                    } else {
                        binding.FRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
            }

            }


    }
    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    private fun setLocale(languageCode: String) {
        val resources = requireContext().resources
        val config = Configuration(resources.configuration)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        ViewCompat.setLayoutDirection(requireActivity().window.decorView, if (languageCode == "ar") ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR)
        replaceFragments(SettingsFragment())
        updateBottomNavigationBarTitles()
    }

    override fun onResume() {
        super.onResume()
        updateBottomNavigationBarTitles()//todo check
        val selectedWindUnit = sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name, "metric")
        val selectedTempUnit = sharedPreferencesManager.getTempUnit(SharedKey.TEMP_UNIT.name, "metric")
        val selctedLocation = sharedPreferencesManager.getlocationChoice(SharedKey.GPS.name, "")
        val languageType = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "")

        when (languageType) {
            "ar" -> binding.arRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            else ->binding.enRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }

        when (selectedWindUnit) {
            "metric" ->  binding.matricRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            else ->  binding.impRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }
        when(selectedTempUnit){
            "metric" -> binding.CRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            "standard"->binding.KRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
            else -> binding.FRdiobtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)

        }
        when(selctedLocation){
        "map" -> binding.mapBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
          else -> binding.gpsBtn.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
        }



    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = (context as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun updateBottomNavigationBarTitles() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val menu = bottomNavigationView.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val titleResourceId = when (i) {
                0 -> R.string.home
                1 -> R.string.favoritemenu
                2 -> R.string.notifications
                3 -> R.string.settings
                else -> throw IllegalArgumentException("Invalid menu item index")
            }
            val localizedTitle = requireContext().getString(titleResourceId)
            menuItem.title = localizedTitle
        }

}

}

