package com.bbg.aiservicemock.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.bbg.aiservicemock.MainApplication
import com.bbg.aiservicemock.logger.Logger
import com.bbg.aiservicemock.repository.gps.GnssTracker
import com.bbg.aiservicemock.repository.gps.GpsRepository
import com.bbg.aiservicemock.repository.gps.GpsTracker
import com.bbg.aiservicemock.repository.lte.LTERepository
import java.util.*
import kotlin.concurrent.timerTask

class AIService : Service() {

    private val logger = Logger("ai service")
    private var timer : Timer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val MIN_TIME_BW_UPDATES = (1000 * 1).toLong()
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 5.0f // 5 meters

    private val locationManager : LocationManager by lazy { MainApplication.applicationContext().getSystemService(
        Context.LOCATION_SERVICE) as LocationManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.info("bootstrap service...")
        startTracker()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun registerLocationManager() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainApplication.applicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            logger.error("[GPS] Permission not granted")
            return
        }
        logger.info("gps in enabled: ${isGpsProviderEnabled()}")
        if (isGpsProviderEnabled()) {
            logger.info("gps provided by system...")
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            locationManager.registerGnssStatusCallback(GnssTracker, handler)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                GpsTracker
            )
        }
    }

    private fun startTracker() {
        registerLocationManager()
        LTERepository.startTelephonyTracker()
    }

    private fun isGpsProviderEnabled() : Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("service destroyed...")
    }
}