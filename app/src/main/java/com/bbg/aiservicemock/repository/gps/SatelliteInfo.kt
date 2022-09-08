package com.bbg.aiservicemock.repository.gps

import android.location.GnssStatus

class SatelliteInfo {
    private var count: Int = 0
    private var cn0DbHzNonZeroCount: Int = 0
    private var totalCn0DbHz: Int = 0
    private var avgCn0DbHz: Int = 0
    private val cn0DbHzArray: ArrayList<Int> = arrayListOf()

    fun reset() {
        count = 0
        cn0DbHzNonZeroCount = 0
        totalCn0DbHz = 0
        avgCn0DbHz = 0
        cn0DbHzArray.clear()
    }

    fun getCount() = cn0DbHzNonZeroCount

    fun getSignalToNoiseRatio() : Int {
        return avgCn0DbHz
    }

    fun getSatelliteCount() : Int {
        return count
    }


    fun add(status: GnssStatus, index: Int) {
        val cn0DbHz = status.getCn0DbHz(index).toInt()
        count++
        totalCn0DbHz += cn0DbHz
        cn0DbHzArray.add(cn0DbHz)
        if (0 < cn0DbHz) {
            cn0DbHzNonZeroCount++
        }
    }

    fun getCn0DbHzArrayStr(): String {
        var cn0DbHzArrayStr:String = ""
        try {
            cn0DbHzArray.forEachIndexed { index, i ->
                if (0 == index)
                    cn0DbHzArrayStr += "$i"
                else
                    cn0DbHzArrayStr += ", $i"
            }
        } catch (e: Exception) {
            //
        }
        return cn0DbHzArrayStr
    }

    fun calculate() {
        if (0 < count)
            avgCn0DbHz = (totalCn0DbHz / count).toInt()
    }

    override fun toString(): String {
        return "$count-$cn0DbHzNonZeroCount-$avgCn0DbHz"
    }
}