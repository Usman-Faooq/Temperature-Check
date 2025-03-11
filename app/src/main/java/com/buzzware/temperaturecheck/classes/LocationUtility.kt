package com.buzzware.temperaturecheck.classes

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class LocationUtility(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationUpdates(callback: (Location) -> Unit) {
        val localCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let { currentLocation ->
                    callback(currentLocation)
                }
            }
        }

        locationCallback = localCallback

        if (checkLocationPermission()) {
            val locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            fusedLocationClient.requestLocationUpdates(locationRequest, localCallback, Looper.getMainLooper())
        }
    }

    fun removeLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    // Retrieve the last known location as a LatLng
    fun getLastKnownLocation(callback: (LatLng, Location) -> Unit) {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val lastLocation = location
                    val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                    callback(latLng, location)
                }
        }
    }
}