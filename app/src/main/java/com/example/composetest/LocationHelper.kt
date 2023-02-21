package com.example.composetest

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class LocationHelper {

    val LOCATION_REFRESH_TIME = 3000 // 3 seconds. The Minimum Time to get location update
    val LOCATION_REFRESH_DISTANCE = 30 // 30 meters. The Minimum Distance to be changed to get location update
    val MY_PERMISSIONS_REQUEST_LOCATION = 100

    var myLocationListener: MyLocationListener? = null

    interface MyLocationListener {
        fun onLocationChanged(location: Location)
    }

    fun getLastLocation(context: Context) : Location? {
        var lastLocation: Location? = null
        //myLocationListener = myListener

        val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

// check for permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // permission is denined by user, you can show your alert dialog here to send user to App settings to enable permission
            } else {
                ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }

//        var targetlocation = Location(LocationManager.GPS_PROVIDER)
//        targetlocation.latitude = 55.555555
//        targetlocation.longitude = 55.555555
//
//
//        mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
//        mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, targetlocation)

        // setMock(40.0,40.0,10.0f,mLocationManager)
        return lastLocation
    }
//    fun mockLocation() {
//        val lm = getSystemService(LOCATION_SERVICE) as LocationManager?
//        val criteria = Criteria()
//        criteria.accuracy = Criteria.ACCURACY_FINE
//        val mocLocationProvider = lm!!.getBestProvider(criteria, true)
//        if (mocLocationProvider == null) {
//            Toast.makeText(
//                ApplicationProvider.getApplicationContext<Context>(),
//                "No location provider found!",
//                Toast.LENGTH_SHORT
//            ).show()
//            return
//        }
//        lm.addTestProvider(
//            mocLocationProvider, false, false,
//            false, false, true, true, true, 0, 5
//        )
//        lm.setTestProviderEnabled(mocLocationProvider, true)
//        val loc = Location(mocLocationProvider)
//        val mockLocation = Location(mocLocationProvider) // a string
//        mockLocation.latitude = -26.902038 // double
//        mockLocation.longitude = -48.671337
//        mockLocation.altitude = loc.altitude
//        mockLocation.time = System.currentTimeMillis()
//        lm.setTestProviderLocation(mocLocationProvider, mockLocation)
//        Toast.makeText(
//            ApplicationProvider.getApplicationContext<Context>(),
//            "Working",
//            Toast.LENGTH_SHORT
//        ).show()
//    }

    public fun setMock(latitude: Double, longitude: Double, accuracy: Float, locMgr: LocationManager) {
        locMgr.addTestProvider(
            LocationManager.GPS_PROVIDER,
            "requiresNetwork" === "",
            "requiresSatellite" === "",
            "requiresCell" === "",
            "hasMonetaryCost" === "",
            "supportsAltitude" === "",
            "supportsSpeed" === "",
            "supportsBearing" === "",
            ProviderProperties.POWER_USAGE_LOW,
            ProviderProperties.ACCURACY_FINE
        )
        val newLocation = Location(LocationManager.GPS_PROVIDER)
        newLocation.latitude = 37.382101// latitude
        newLocation.longitude = 127.119278// longitude
        newLocation.accuracy = accuracy
        newLocation.altitude = 0.0
        newLocation.accuracy = 500f
        newLocation.time = System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        locMgr.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
        locMgr.setTestProviderStatus(
            LocationManager.GPS_PROVIDER,
            LocationProvider.AVAILABLE,
            null, System.currentTimeMillis()
        )
        locMgr.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation)
    }
    fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
        myLocationListener = myListener

        val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

        val mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                //your code here
                myLocationListener!!.onLocationChanged(location) // calling listener to inform that updated location is available
                //setMock(40.0,40.0,10.0f,mLocationManager)
            }
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                //setMock(40.0,40.0,10.0f,mLocationManager)
            }
            override fun onProviderEnabled(provider: String) {
                //setMock(40.0,40.0,10.0f,mLocationManager)
            }
            override fun onProviderDisabled(provider: String) {}
        }
// check for permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME.toLong(),LOCATION_REFRESH_DISTANCE.toFloat(), mLocationListener)
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // permission is denined by user, you can show your alert dialog here to send user to App settings to enable permission
            } else {
                ActivityCompat.requestPermissions(context,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
    }

}