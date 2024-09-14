package com.ujjolch.masterapp

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.LocationManager
import android.net.ConnectivityManager
import java.text.NumberFormat
import java.util.Locale
import android.provider.Settings
import android.util.Log
import co.yml.charts.common.extensions.isNotNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.ujjolch.masterapp.Result
import kotlinx.coroutines.tasks.await

fun isBluetoothEnabled(context: Context): Boolean {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return bluetoothManager.adapter?.isEnabled == true
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo?.isConnected == true
}
// Request code for enabling Bluetooth
private const val REQUEST_ENABLE_BT = 1

@SuppressLint("MissingPermission")
fun requestEnableBluetooth(activity: Activity) {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
        // Handle the situation appropriately in your app
    } else {
        if (!bluetoothAdapter.isEnabled) {
            // Bluetooth is not enabled, prompt the user to enable it
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            // Bluetooth is already enabled
            // You can continue with Bluetooth operations
        }
    }
}

fun requestEnableLocation(activity: Activity) {
    val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        // Location is not enabled, prompt the user to enable it
        val enableLocationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(enableLocationIntent)
    } else {
        // Location is already enabled
        // You can continue with location-related operations
    }
}
fun openWiFiSettings(activity: Activity) {
    val wifiSettingsIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
    activity.startActivity(wifiSettingsIntent)
}

//fun setLocale(context: Context, languageCode: String) { //for setting the language
//    val locale = Locale(languageCode)
//    Locale.setDefault(locale)
//
//    val config = Configuration()
//    config.setLocale(locale)
//    context.resources.updateConfiguration(config, context.resources.displayMetrics)
//}
fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    // Set the numeric system to English
    config.setLayoutDirection(locale)
    context.createConfigurationContext(config)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Update the Locale for number formatting
    val englishLocale = Locale("en")
    Locale.setDefault(englishLocale)
    val arabicFormat = NumberFormat.getInstance(englishLocale) //This sets the Locale for the numbers

}


fun saveLanguage(context: Context, languageCode: String) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    with(prefs.edit()) {
        putString("selected_language", languageCode)
        apply()
    }
}

fun getSavedLanguage(context: Context): String? {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getString("selected_language", "en")  // Default to English
}

fun restartActivity(context: Context) {
    val intent = (context as? Activity)?.intent
    intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)

}

fun isUserSignedInWithGoogle(): Boolean {
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Check if the user is signed in
    currentUser?.let {
        // Loop through the provider data to see if Google is one of them
        for (userInfo in it.providerData) {
            if (userInfo.providerId == GoogleAuthProvider.PROVIDER_ID) {
                return true
            }
        }
    }
    // Return false if Google is not found among the providers
    return false
}

suspend fun checkIfFirstTimeUser(): Result<Boolean> {
    val currentUser = FirebaseAuth.getInstance().currentUser?.email
    val db = FirebaseFirestore.getInstance()

    return try {
        if (currentUser != null) {
            val userDocRef = db.collection("users").document(currentUser)
                .collection("googlesignin").document("gs")

            val document = userDocRef.get().await() // Await for the result

            if (document.exists()) {
                // User has signed in before
                Log.d("SignInCheck", "User is not signing in for the first time.")
                Result.Success(false)
            } else {
                // First time sign-in
                Log.d("SignInCheck", "User is signing in for the first time.")
                Result.Success(true)
            }
        } else {
            Result.Success(false)
        }
    } catch (e: Exception) {
        Log.d("SignInCheck", "Error checking user document", e)
        Result.Error(e)
    }
}

fun createUserRecord() {
    val currentUser = FirebaseAuth.getInstance().currentUser?.email
    val db = FirebaseFirestore.getInstance()

    val newUser = hashMapOf(
        "no" to 1,
        "s" to 2
        // Add other user data as necessary
    )

    if(currentUser.isNotNull()) {
        db.collection("users").document(currentUser!!).collection("googlesignin").
        document("gs")
            .set(newUser)
            .addOnSuccessListener {
                Log.d("SignInCheck", "User record created successfully.")
            }.addOnFailureListener { exception ->
                Log.d("SignInCheck", "Error creating user record", exception)
            }
    }
}