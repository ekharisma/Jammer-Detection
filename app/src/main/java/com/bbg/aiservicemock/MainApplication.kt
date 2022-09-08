package com.bbg.aiservicemock

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    init {
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: MainApplication? = null

        fun applicationContext() : Context {
            return INSTANCE!!.applicationContext
        }
    }
}