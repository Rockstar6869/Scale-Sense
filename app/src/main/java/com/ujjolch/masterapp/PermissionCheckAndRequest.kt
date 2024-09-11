package com.ujjolch.masterapp

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat


fun hasBluetoothPermissions(context: Context): Boolean {
    val bluetoothPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH
    ) == PackageManager.PERMISSION_GRANTED

    val bluetoothAdminPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH_ADMIN
    ) == PackageManager.PERMISSION_GRANTED

    val bluetoothConnectPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH_CONNECT
    ) == PackageManager.PERMISSION_GRANTED

    val bluetoothScanPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH_SCAN
    ) == PackageManager.PERMISSION_GRANTED

    return bluetoothPermission && bluetoothAdminPermission &&
            bluetoothConnectPermission && bluetoothScanPermission
}

fun hasLocationPermissions(context: Context): Boolean {
    val fineLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val coarseLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return fineLocationPermission && coarseLocationPermission
}


// Request code for permissions
private const val REQUEST_BLUETOOTH_PERMISSIONS = 1001

fun requestBluetoothPermissions(activity: Activity) {
    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    val permissionsToRequest = bluetoothPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        // Check if any of the permissions are permanently denied
        val permanentlyDenied = permissionsToRequest.any {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (permanentlyDenied) {
            // Permissions are permanently denied, navigate to app settings
            navigateToAppSettings(activity)
        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        }
    } else {
        // All permissions are already granted, proceed with Bluetooth operations
    }
}
// Request code for location permissions
private const val REQUEST_LOCATION_PERMISSIONS = 1002

fun requestLocationPermissions(activity: Activity) {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Check which permissions are not granted
    val permissionsToRequest = locationPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        // Check if any of the permissions are permanently denied
        val permanentlyDenied = permissionsToRequest.any {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (permanentlyDenied) {
            // Permissions are permanently denied, navigate to app settings
            navigateToAppSettings(activity)
        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_LOCATION_PERMISSIONS
            )
        }
    } else {
        // All permissions are already granted, proceed with location operations
    }
}
fun requestBluetoothPermissionsWithoutNavigation(activity: Activity) { //Requests bluetooth permissions only if not blocked by android
    val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    val permissionsToRequest = bluetoothPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        // Check if any of the permissions are permanently denied
        val permanentlyDenied = permissionsToRequest.any {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (permanentlyDenied) {
            // Permissions are permanently denied, navigate to app settings
//            navigateToAppSettings(activity) //We will not navigate here since permissions denied
        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        }
    } else {
        // All permissions are already granted, proceed with Bluetooth operations
    }
}
fun requestLocationPermissionsWithoutNavigation(activity: Activity) {
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Check which permissions are not granted
    val permissionsToRequest = locationPermissions.filter {
        ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
    }

    if (permissionsToRequest.isNotEmpty()) {
        // Check if any of the permissions are permanently denied
        val permanentlyDenied = permissionsToRequest.any {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

        if (permanentlyDenied) {
            // Permissions are permanently denied, navigate to app settings
//            navigateToAppSettings(activity) //We will not navigate here since permissions denied
        } else {
            // Request the permissions
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_LOCATION_PERMISSIONS
            )
        }
    } else {
        // All permissions are already granted, proceed with location operations
    }
}
private fun showPermissionRationaleDialog(activity: Activity) {
    AlertDialog.Builder(activity).apply {
        setTitle("Bluetooth Permissions Required")
        setMessage("This app requires Bluetooth permissions to function properly.")
        setPositiveButton("Grant") { _, _ ->
            // Request the permissions after showing rationale
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                ),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        }
        setNegativeButton("Cancel", null)
        show()
    }
}

// Handle the case when permissions are permanently denied
fun handlePermissionsResult(
    activity: Activity,
    requestCode: Int,
    grantResults: IntArray,
    permissions: Array<out String>
) {
    if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            // All permissions granted, proceed with Bluetooth operations
        } else {
            // Check if the permissions are permanently denied
            val permanentlyDenied = permissions.any {
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }

            if (permanentlyDenied) {
                // Redirect to app settings to manually enable permissions
                navigateToAppSettings(activity)
            }
        }
    }
}

// Navigate to the app settings screen where the user can manually enable permissions
private fun navigateToAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}
