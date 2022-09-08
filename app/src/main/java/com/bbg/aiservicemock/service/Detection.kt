package com.bbg.aiservicemock.service

import com.bbg.aiservicemock.logger.Logger
import com.bbg.aiservicemock.repository.gps.GpsRepository
import com.bbg.aiservicemock.repository.lte.LTERepository
import java.util.*
import kotlin.concurrent.timerTask


class Detection : IDetection {
    private val logger = Logger("Detection")
    private var statisticallyAbnormal = false
    private var routineTimer : Timer? = null
    private val lteList = mutableListOf<Int>()
    private val snrList = mutableListOf<Int>()
    private val supvList = mutableListOf<Int>()

    override fun isActive(): Boolean {
        return true
    }

    override fun process() {
        routineTimer = Timer()
        val lteRssi = LTERepository.getLteRssi()
        val snr = GpsRepository.getSignalToNoiseRatio()
        val supv = GpsRepository.getSatelliteUsedPerViewRatio()
        routineTimer?.schedule(timerTask {
            routine(lteRssi, snr, supv)
        }, 1 * 1_000L, 10 * 1_000L)
    }

    private fun routine(lteRssi: Int, snr: Int, supv: Int) {
        if ((lteList.size < 30) && (snrList.size < 30) && (supvList.size < 30)) {
            lteList.add(lteRssi)
            snrList.add(snr)
            supvList.add(supv)
        } else {
            if (statisticallyAbnormal) {

            }
        }
    }

    override fun emitData(deviceId: String): ByteArray {
        val mock = "mock"
        return mock.toByteArray()
    }
}