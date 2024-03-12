package com.example.weatherapppoject.view

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.weatherapppoject.R
import com.example.weatherapppoject.databinding.FragmentSettingsBinding
import com.example.weatherapppoject.sharedprefrences.SharedKey
import com.example.weatherapppoject.sharedprefrences.SharedPrefrencesManager
import com.example.weatherapppoject.view.ContextUtils
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
        // Get the SharedPreferencesManager instance
        sharedPreferencesManager = SharedPrefrencesManager.getInstance(requireContext())

          }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocale(sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "default"))
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
                        // Save data
                        sharedPreferencesManager.saveString(SharedKey.LANGUAGE.name, "ar")

                    }
                    R.id.enRdiobtn -> {
                        binding.arRdiobtn.isChecked = false
                        Toast.makeText(requireContext(),"ar",Toast.LENGTH_SHORT).show()
                        sharedPreferencesManager.saveString(SharedKey.LANGUAGE.name, "en")

                        setLocale("en")
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

        binding.textView9.text = translatedSettingsText
        binding.textView10.text = translatedLanguageText

//        requireActivity().recreate()

    }
    override fun onResume() {
        super.onResume()
        val selectedLanguage = sharedPreferencesManager.getString(SharedKey.LANGUAGE.name, "en")
        when (selectedLanguage) {
            "ar" -> binding.arRdiobtn.isChecked = true
            "en" -> binding.enRdiobtn.isChecked = true
        }
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

