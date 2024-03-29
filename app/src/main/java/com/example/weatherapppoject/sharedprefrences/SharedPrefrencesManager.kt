package com.example.weatherapppoject.sharedprefrences

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

class SharedPrefrencesManager private constructor(context: Context){
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
   fun saveLanguage(key: String, value: String) { sharedPreferences.edit().putString(key, value).apply() }
    fun getLanguae(key: String, defaultValue: String): String { return sharedPreferences.getString(key, defaultValue) ?: defaultValue }

    fun saveWindUnit(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply() }
    fun getWindUnit(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveTempUnit(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply() }
    fun getTempUnit(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveUnitsType(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getUnitsType(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveCountryName(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getCountryName(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    fun saveLocationFromMap(key: String, longt: Double, lat:Double) {
    val editor = sharedPreferences.edit()
    editor.putString(key + "_longt", longt.toString())
    editor.putString(key + "_lat", lat.toString())
    editor.apply()
}
    fun savelocationChoice(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun getlocationChoice(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun getLocationFromMap(key: String): Pair<Double, Double>? {
        val longtKey = key + "_longt"
        val latKey = key + "_lat"
        val longt = sharedPreferences.getString(longtKey, null)?.toDoubleOrNull()
        val lat = sharedPreferences.getString(latKey, null)?.toDoubleOrNull()

        if (longt != null && lat != null) {
            return Pair(longt, lat)
        }

        return null
    }

    fun saveLocationToHOme(key: String, longt: Double, lat:Double) {
        val editor = sharedPreferences.edit()
        editor.putString(key + "_longt", longt.toString())
        editor.putString(key + "_lat", lat.toString())
        editor.apply()
    }

    fun getLocationToHOme(key: String): Pair<Double, Double>? {
        val longtKey = key + "_longt"
        val latKey = key + "_lat"
        val longt = sharedPreferences.getString(longtKey, null)?.toDoubleOrNull()
        val lat = sharedPreferences.getString(latKey, null)?.toDoubleOrNull()

        if (longt != null && lat != null) {
            return Pair(longt, lat)
        }

        return null
    }

    fun saveLocationToAlert(key: String, longt: Double, lat:Double) {
        val editor = sharedPreferences.edit()
        editor.putString(key + "_longt", longt.toString())
        editor.putString(key + "_lat", lat.toString())
        editor.apply()
    }

    fun getLocationToAlert(key: String): Pair<Double, Double>? {
        val longtKey = key + "_longt"
        val latKey = key + "_lat"
        val longt = sharedPreferences.getString(longtKey, null)?.toDoubleOrNull()
        val lat = sharedPreferences.getString(latKey, null)?.toDoubleOrNull()

        if (longt != null && lat != null) {
            return Pair(longt, lat)
        }

        return null
    }


    fun savecurrentLocationToMap(key: String, latlang: LatLng) {
        val editor = sharedPreferences.edit()
        editor.putString(key + "_longt", latlang.latitude.toString())
        editor.putString(key + "_lat", latlang.longitude.toString())
        editor.apply()
    }

    fun getcurrentLocationToMap(key: String): LatLng? {
        val longtKey = key + "_longt"
        val latKey = key + "_lat"
        val longt = sharedPreferences.getString(longtKey, null)?.toDoubleOrNull()
        val lat = sharedPreferences.getString(latKey, null)?.toDoubleOrNull()

        if (longt != null && lat != null) {
            return LatLng(lat,longt)
        }

        return null
    }

    fun setMap(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun getSavedMap(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
        companion object {
                private const val PREF_NAME = "MyAppPreferences"
                private var instance: SharedPrefrencesManager? = null

                @OptIn(InternalCoroutinesApi::class)
                fun getInstance(context: Context): SharedPrefrencesManager {
                        return instance ?: synchronized(this) {
                                instance ?: SharedPrefrencesManager(context).also { instance = it }
                        }
                }
        }
        }


enum class SharedKey {
        LANGUAGE,
        GPS, //location choice ==> gps or map
        MAP, //type of the map ==> home or fav or alert.
        Home, // save lan and long to home
        FAV, //save lan and long to fave
        ALERT,
        UNITS,
        CURMAP,
        TEMP_UNIT,
        ALERT_TYPE,
    WIND


}
