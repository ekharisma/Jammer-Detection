package com.bbg.aiservicemock.service

interface IDetection {
    fun isActive() : Boolean
    fun process()
    fun emitData(deviceId : String) : ByteArray
}