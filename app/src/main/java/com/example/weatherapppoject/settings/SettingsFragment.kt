package com.example.weatherapppoject.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.FragmentSettingsBinding
import com.example.weatherapppoject.map.view.MapsFragment
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
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
        setLocale(sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "default"))

        binding.locationToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                R.id.button1 -> {
                    if (isChecked) {
                        binding.button1.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.white)
                        binding.button3.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.buttons)
                        sharedPreferencesManager.savelocationChoice(SharedKey.GPS.name , "map")
                        sharedPreferencesManager.setMap(SharedKey.MAP.name,"home")
                        sharedPreferencesManager.saveLanguage(SharedKey.GPS.name, "map")
                        replaceFragments(MapsFragment())
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
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = requireContext().resources

        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        val context= ContextUtils.wrapContext(requireContext(), locale)
        val translatedSettingsText = context.getString(R.string.settings)
        val translatedLanguageText = context.getString(R.string.language)

        binding.tvSettings.text = translatedSettingsText
        binding.textView10.text = translatedLanguageText

    }
    override fun onResume() {
        super.onResume()
        val selectedLanguage = sharedPreferencesManager.getLanguae(SharedKey.LANGUAGE.name, "en")
        when (selectedLanguage) {
            "ar" -> binding.arRdiobtn.isChecked = true
            "en" -> binding.enRdiobtn.isChecked = true
        }
    }
    private fun replaceFragments(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.addToBackStack(null) // Optional: Adds the transaction to the back stack
        transaction.commit()
    }
}


object ContextUtils {
    fun wrapContext(baseContext: Context, locale: Locale): Context {
        val config = Configuration(baseContext.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        return baseContext.createConfigurationContext(config)
    }
}

