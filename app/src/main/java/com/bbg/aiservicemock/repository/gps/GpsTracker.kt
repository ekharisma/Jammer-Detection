package com.bbg.aiservicemock.repository.gps

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import com.bbg.aiservicemock.logger.Logger

object GpsTracker : LocationListener {
    private var mDrGpsInitLogCounter = 0
    private var mLocation : Location = Location(LocationManager.GPS_PROVIDER)
    private val DRGPS_INIT_LOG_MAX_COUNT = 5
    private val mDRLocation : Location = Location(LocationManager.GPS_PROVIDER)
    private val logger = Logger("GPSTracker")

    override fun onLocationChanged(location: Location) {
        location.let {
            mLocation = it
        }
    }
    private fun setPosition(
        gpsLat: Double,
        gpsLon: Double,
        locationStatus: Int,
        safetyStatus: Int,
        angle: Double,
        lat: Double,
        lon: Double
    ) {
        // int locationStatus   : 측위 상태
        //    0 : No Initialize (GPS)
        //    1 : Find
        //    2 : Bad
        //    80: GPS 미수신
        //    99: 측위 불가
        // int safetyStatus     : Reserved
        if (0.0 == gpsLat || 0.0 == gpsLon) {
            if (0 == mDrGpsInitLogCounter) {
                logger.debug("[DRGPS] locationChanged : locationStatus = $locationStatus, safetyStatus = $safetyStatus, " +
                        "angle = ${angle.toFloat()}, gpsLat = $gpsLat, gpsLon = $gpsLon, lat = $lat, lon = $lon")
            }
            mDrGpsInitLogCounter = (mDrGpsInitLogCounter +1) % DRGPS_INIT_LOG_MAX_COUNT
        } else {
            mDrGpsInitLogCounter = 0
            logger.debug(
                "[DRGPS] locationChanged : locationStatus = $locationStatus, safetyStatus = $safetyStatus, " +
                        "angle = ${angle.toFloat()}, gpsLat = $gpsLat, gpsLon = $gpsLon, lat = $lat, lon = $lon"
            )
        }
        mDRLocation.let {
            it.longitude = lon
            it.latitude = lat
            it.bearing = angle.toFloat()
            logger.info("[DRGPS] locationChanged : ($gpsLat, $gpsLon) -> (${it.latitude}, ${it.longitude})")
        }
    }
}
