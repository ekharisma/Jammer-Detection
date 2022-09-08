package com.bbg.aiservicemock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bbg.aiservicemock.logger.Logger
import com.bbg.aiservicemock.repository.gps.GpsRepository
import com.bbg.aiservicemock.repository.lte.LTERepository
import com.bbg.aiservicemock.service.AIService
import org.w3c.dom.Text
import java.util.*
import kotlin.concurrent.timerTask

class DetectorActivity : AppCompatActivity() {
    private val logger = Logger("Detector Activity")
    private var timer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detector)
        startAIService()
        initText()
    }

    private fun startAIService() {
        logger.info("initiate service...")
        val intent = Intent(this, AIService::class.java)
        startService(intent)
    }

    private fun initText() {
        val lteTxt = findViewById<TextView>(R.id.lteTxt)
        val snrTxt = findViewById<TextView>(R.id.snrTxt)
        val supvTxt = findViewById<TextView>(R.id.supvTxt)
        timer = Timer()
        timer?.schedule(timerTask {
            runOnUiThread {
                lteTxt.text = "LTE RSSI = ${LTERepository.getLteRssi()}"
                snrTxt.text = "Signal to Noise = ${GpsRepository.getSignalToNoiseRatio()}"
                supvTxt.text = "Satellite used per view = ${GpsRepository.getSignalToNoiseRatio()}"
            }
        }, 1 * 1_000L, 2 * 1_000L)
    }

}