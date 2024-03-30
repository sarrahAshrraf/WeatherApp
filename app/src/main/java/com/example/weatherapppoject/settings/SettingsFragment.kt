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
import android.widget.RadioButton
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
//        setLocale(sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default"))

        binding.locationToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.button1 -> {
                    if (isChecked) {
                        binding.button1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.button3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.savelocationChoice(SharedKey.GPS.name , "map")
                        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")
                        sharedPreferencesManager.saveLanguage(SharedKey.GPS.name, "map")
                        if(checkForInternet(requireContext())){
                            replaceFragments(MapsFragment())
                        }
                        else {
                            Toast.makeText(requireContext(),"check yur netwrok",Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        binding.button1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
                R.id.button3 -> {
                    if (isChecked) {
                        binding.button3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.button1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.savelocationChoice(SharedKey.GPS.name , "gps")
                        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")



                    } else {
                        binding.button3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                    }
                }
            }
        }

        binding.langueRadioGrop.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<RadioButton>(checkedId)
            if (!checkedRadioButton.isChecked) {
                checkedRadioButton.isChecked = true
            } else {
                when (checkedId) {
                    R.id.arRdiobtn -> {
                        binding.enRdiobtn.isChecked = false
                        Toast.makeText(requireContext(),"en", Toast.LENGTH_SHORT).show()
                        setLocale("ar")
                        sharedPreferencesManager.saveLanguage(SharedKey.LANGUAGE.name, "ar")

                    }
                    R.id.enRdiobtn -> {
                        binding.arRdiobtn.isChecked = false
                        Toast.makeText(requireContext(),"ar",Toast.LENGTH_SHORT).show()
                        sharedPreferencesManager.saveLanguage(SharedKey.LANGUAGE.name, "en")

                        setLocale("en")
                    }
                }
            }
        }

        binding.unitsRadioGrop.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<RadioButton>(checkedId)
            if (!checkedRadioButton.isChecked) {
                checkedRadioButton.isChecked = true
            } else {
                when (checkedId) {
                    R.id.matricRdiobtn -> {
                        binding.impRdiobtn.isChecked = false
                        Toast.makeText(requireContext(),"metric", Toast.LENGTH_SHORT).show()
                        sharedPreferencesManager.saveUnitsType(SharedKey.UNITS.name, "metric") //m/s

                    }
                    R.id.impRdiobtn -> {
                        binding.matricRdiobtn.isChecked = false
                        Toast.makeText(requireContext(),"impa",Toast.LENGTH_SHORT).show()
                        sharedPreferencesManager.saveUnitsType(SharedKey.UNITS.name, "imperial") // miles/hrs

                    }
                }
            }
        }


        binding.tempUnitsRadioGrop.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById<RadioButton>(checkedId)
            if (!checkedRadioButton.isChecked) {
                checkedRadioButton.isChecked = true
            } else {
                when (checkedId) {//c
                    R.id.CRdiobtn -> {
                        binding.KRdiobtn.isChecked = false
                        binding.FRdiobtn.isChecked = false

                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "metric")

                    }
                    R.id.KRdiobtn -> {///k
                        binding.CRdiobtn.isChecked = false
                        binding.FRdiobtn.isChecked = false
                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "standard")
                    }
                    R.id.FRdiobtn -> {///k
                        binding.KRdiobtn.isChecked = false
                        binding.CRdiobtn.isChecked = false
                        sharedPreferencesManager.saveTempUnit(SharedKey.TEMP_UNIT.name, "imperial")
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
    private fun recreateFragment() {
        // Save any necessary data or state here

        val currentFragment = parentFragmentManager.findFragmentById(R.id.frameLayout)
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        // Recreate the fragment
        fragmentTransaction.detach(currentFragment!!)
        fragmentTransaction.attach(currentFragment)
        fragmentTransaction.commit()
    }

//    private fun setLocale(language: String) {
//                val resources = requireContext().resources
//        val config = Configuration(resources.configuration)
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//        config.setLocale(locale)
//        resources.updateConfiguration(config, resources.displayMetrics)
//        ViewCompat.setLayoutDirection(requireActivity().window.decorView, if (language == "ar") ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR)
//        recreateFragment()
//
////        settingsViewModel.putBooleanInSharedPreferences("isLayoutChangedBySettings", true)
//
//
//    }

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
//    private fun setLocale(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//        val resources = requireContext().resources
//
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//        val context= ContextUtils.wrapContext(requireContext(), locale)
//        val translatedSettingsText = context.getString(R.string.settings)
//        val translatedLanguageText = context.getString(R.string.language)
//
//        binding.tvSettings.text = translatedSettingsText
//        binding.textView10.text = translatedLanguageText
//
//    }
    override fun onResume() {
        super.onResume()
        updateBottomNavigationBarTitles()//todo check
        val selectedWindUnit = sharedPreferencesManager.getUnitsType(SharedKey.UNITS.name, "metric")
        val selectedTempUnit = sharedPreferencesManager.getTempUnit(SharedKey.TEMP_UNIT.name, "metric")
        val selctedLocation = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "")

        when (selectedWindUnit) {
            "metric" -> binding.matricRdiobtn.isChecked = true
            else -> binding.impRdiobtn.isChecked = true

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


//object ContextUtils {
//    fun wrapContext(baseContext: Context, locale: Locale): Context {
//        val config = Configuration(baseContext.resources.configuration)
//        Locale.setDefault(locale)
//        config.setLocale(locale)
//        return baseContext.createConfigurationContext(config)
//    }
}

