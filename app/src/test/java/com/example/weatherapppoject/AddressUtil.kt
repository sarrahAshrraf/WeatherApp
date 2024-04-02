package com.example.weatherapppoject

import android.content.Context
import android.location.Address
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

class AddressUtil {

    suspend fun getAddressFromCoordinates(latitude: Double, longitude: Double, context: Context): String =
        withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context)

            try {
                val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

                if (addresses?.isNotEmpty() == true) {
                    val address: Address? = addresses[0]
                    val addressStringBuilder = StringBuilder()

                    if (address != null) {
                        for (i in 0..address.maxAddressLineIndex) {
                            addressStringBuilder.append(address.getAddressLine(i))
                            if (i < address.maxAddressLineIndex) {
                                addressStringBuilder.append(", ")
                            }
                        }
                    }

                    addressStringBuilder.toString()
                } else {
                    "No address found"
                }
            } catch (e: IOException) {
                "Error: Geocoder service not available"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
}