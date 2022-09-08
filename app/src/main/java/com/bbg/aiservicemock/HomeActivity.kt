package com.bbg.aiservicemock

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bbg.aiservicemock.logger.Logger
import com.bbg.aiservicemock.service.AIService


class HomeActivity : AppCompatActivity() {

    private val logger = Logger("HomeActivity")
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initBtn()
    }

    private fun initBtn() {
        val jammerDetectorBtn = findViewById<Button>(R.id.jammerDetectorBtn)
        jammerDetectorBtn.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (isPermissionFulfilled()) {
            Toast.makeText(this, "permission is granted", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, DetectorActivity::class.java))
        } else {
            requestAppPermission()
            startActivity(Intent(applicationContext, DetectorActivity::class.java))
        }
    }

    private fun isPermissionFulfilled() : Boolean {
        val permissionList = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        var isPermissionOk = true
        for (permission in permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                isPermissionOk = false
            }
        }
        return isPermissionOk
    }

    private fun requestAppPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog
                .setTitle("Permission Needed")
                .setMessage("This permission is needed to access location")
                .setPositiveButton("Sip!") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        REQUEST_CODE
                    )
                }
                .setNegativeButton("Moh!") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            logger.info("permission: $permission")
        }
        for (grantResult in grantResults) {
            logger.info("grantResult: $grantResult")
        }
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}