package com.bbg.aiservicemock.repository.lte

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.*
import androidx.core.app.ActivityCompat
import com.bbg.aiservicemock.MainApplication
import com.bbg.aiservicemock.logger.Logger

object LTERepository {
    private var telephonyManager : TelephonyManager? = null
    private var lteRssi = 0
    private val logger = Logger("LTE Repository")

    fun startTelephonyTracker() {
        telephonyManager = MainApplication.applicationContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager?.registerTelephonyCallback(
                MainApplication.applicationContext().mainExecutor,
                object : TelephonyCallback(), TelephonyCallback.SignalStrengthsListener {
                    override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
                        telephonyManager?.let {
                            lteRssi = setLteRSSI(it, MainApplication.applicationContext())
                            // using new implementation, dbm value is in negative (-x dbm)
                            // so we need to invert it
                            lteRssi *= -1
                        }
                    }

                }
            )
        }
        else {
            telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
        }
    }

    private val phoneStateListener = object : PhoneStateListener() {
        override fun onSignalStrengthsChanged(signalStrength: SignalStrength?) {
            super.onSignalStrengthsChanged(signalStrength)
            telephonyManager?.let {
                if (signalStrength != null) {
                    lteRssi = setLteRSSI(it, MainApplication.applicationContext())
                }
            }
        }
    }

    private fun setLteRSSI(telephonyManager: TelephonyManager, context: Context): Int {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return 0
        }
        val cellInfoList = telephonyManager.allCellInfo
        for (cellInfo in cellInfoList) {
            if (cellInfo is CellInfoLte) {
                lteRssi = cellInfo.cellSignalStrength.dbm
                return lteRssi
            }
        }
        return 0
    }

    fun getLteRssi() : Int {
        return lteRssi
    }
}