package com.example.weatherapppoject.sharedprefrences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

//make it single tone
class SharedPrefrencesManager private constructor(context: Context){

                private val sharedPreferences: SharedPreferences =
                        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

                fun saveString(key: String, value: String) {
                        sharedPreferences.edit().putString(key, value).apply()
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

                fun getString(key: String, defaultValue: String): String {
                        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
                }

                fun removeKey(key: String) {
                        sharedPreferences.edit().remove(key).apply()
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
        GPS


}