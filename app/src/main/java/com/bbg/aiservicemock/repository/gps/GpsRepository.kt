package com.bbg.aiservicemock.repository.gps

object GpsRepository {
    fun getSatelliteUsedPerViewRatio(): Int {
        return GnssTracker.getSatelliteUsedPerViewRatio()
    }

    fun getSignalToNoiseRatio(): Int {
        return GnssTracker.getAvgSignalToNoise()
    }
}