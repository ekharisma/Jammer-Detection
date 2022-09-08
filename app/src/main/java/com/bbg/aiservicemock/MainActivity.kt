package com.bbg.aiservicemock

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import com.bbg.aiservicemock.logger.Logger
import kotlin.math.log

open class MainActivity : AppCompatActivity() {

    private val logger = Logger("MainActivity")
    private val looper = Looper.myLooper()
    private val handler = looper?.let { Handler(it) }
    private val SPLASH_DELAY : Long = 1_000
    private var startTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        logger.info("on create...")
    }

    override fun onStart() {
        super.onStart()
        doWait()
    }

    private val splashRunnable : Runnable by lazy {
        Runnable {
            if (!isFinishing) {
                if (isReady()) {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                } else {
                    doWait()
                }
            }
        }
    }

    private fun isReady(): Boolean {
        val timeDiff = System.currentTimeMillis() - startTime
        return SPLASH_DELAY <= timeDiff
    }

    private fun doWait() {
        handler?.postDelayed(splashRunnable, SPLASH_DELAY)
    }
}