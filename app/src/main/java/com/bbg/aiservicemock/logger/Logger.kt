package com.bbg.aiservicemock.logger

import android.util.Log
import java.util.logging.Logger

class Logger(val tag: String) {
    fun info(msg : String) {
        Log.i(tag, msg)
    }

    fun debug(message: String) {
        Log.d(tag, message)
    }

    fun warn(message: String) {
        Log.w(tag, message)
    }

    fun error(message: String) {
        Log.e(tag, message)
    }

    fun error(message: String, e: Throwable) {
        Log.e(tag, message, e)
    }

}