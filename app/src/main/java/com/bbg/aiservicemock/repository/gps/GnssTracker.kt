package com.bbg.aiservicemock.repository.gps

import android.location.GnssStatus
import com.bbg.aiservicemock.logger.Logger
import kotlin.math.log

object GnssTracker: GnssStatus.Callback() {
    private val logger = Logger("GnssTracker")
    private val satelliteViewInfo = SatelliteInfo()
    private val satelliteUsedInfo = SatelliteInfo()
    private var satelliteUsedCount = 0
    private var satelliteViewCount = 0
    private var avgSignalToNoise = 0

    fun getAvgSignalToNoise() = avgSignalToNoise

    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)
        logger.info("satellite status changed")
        status.let {
            try {
                satelliteViewInfo.reset()
                satelliteUsedInfo.reset()

                logger.info("[GPS] satellite count: ${it.satelliteCount}")

                if (it.satelliteCount > 0) {
                    for (i in 0 until it.satelliteCount) {
                        logger.info("adding satellite view...")
                        satelliteViewInfo.add(it, i)
                        if (it.usedInFix(i)) {
                            logger.info("satellite used...")
                            satelliteUsedInfo.add(it, i)
                        }
                    }
                    satelliteUsedInfo.calculate()
                    satelliteViewInfo.calculate()

                    satelliteUsedCount = satelliteUsedInfo.getSatelliteCount()
                    satelliteViewCount = satelliteUsedInfo.getSatelliteCount()
                    avgSignalToNoise = satelliteUsedInfo.getSignalToNoiseRatio()
                }
            } catch (e : Exception) {

            }
        }
    }

    fun getSatelliteUsedPerViewRatio() : Int {
        val satelliteUsedPerViewRatio = (satelliteUsedCount.toFloat() / satelliteViewCount.toFloat())
        return satelliteUsedPerViewRatio.toInt()
    }
}