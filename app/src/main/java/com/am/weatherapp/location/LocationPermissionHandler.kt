package com.am.weatherapp.location

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationPermissionHandler(
    private val activity: Activity,
    private val permissionLauncher: ActivityResultLauncher<String>,
    private val onPermissionResult: (Boolean) -> Unit
) {

    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${activity.packageName}")
        }
        activity.startActivity(intent)
    }

    fun handlePermissionResult(isGranted: Boolean) {
        onPermissionResult(isGranted)
    }
}