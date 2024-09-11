package com.ujjolch.masterapp

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.yml.charts.common.extensions.isNotNull
import kotlinx.coroutines.delay

@Composable
fun SnackBarForHomeScreen(){
    val context = LocalContext.current
    val activity = context as? Activity
    var hasbltpermissions  by remember {
        mutableStateOf(false)
    }
    var haslocpermissions  by remember {
        mutableStateOf(false)
    }
    var isBltOn by remember {
        mutableStateOf(false)
    }
    var isLocOn by remember {
        mutableStateOf(false)
    }
    var showSnackBar by remember {
        mutableStateOf(false)
    }
    var snackbartxt by remember {
        mutableStateOf("")
    }
    var onStartClick:() -> Unit = {}
    LaunchedEffect(Unit) {
        while (true){
            hasbltpermissions = hasBluetoothPermissions(context)
            haslocpermissions = hasLocationPermissions(context)
            isBltOn = isBluetoothEnabled(context)
            isLocOn  = isLocationEnabled(context)
            Log.d("wfef","$hasbltpermissions $showSnackBar")
            delay(2000)
        }
    }
    LaunchedEffect(hasbltpermissions,haslocpermissions,isBltOn,isLocOn) {
        if(hasbltpermissions && haslocpermissions && isBltOn && isLocOn){
            showSnackBar = false
        }
    }
    if(hasbltpermissions){
        if(isBltOn){
            //Bluetooth is on. Check for Location
            if(haslocpermissions){
                if(isLocOn){
                    //Blt and loc is on and have permissions.
                    showSnackBar = false
                }
                else{
                    showSnackBar = true
                    snackbartxt = "LOCATION IS DISABLED"
                    onStartClick = {
                        if(activity.isNotNull()) {
                            requestEnableLocation(activity!!)
                        }
                    }
                }
            }
            else{
                //No location permissions.
                showSnackBar = true
                snackbartxt = "LOCATION IS DISABLED"
                onStartClick = {
                    if(activity.isNotNull()) {
                        requestLocationPermissions(activity!!)
                    }
                }
            }
        }
        else{
            showSnackBar = true
            snackbartxt = "BLUETOOTH IS DISABLED"
            onStartClick = {
                if(activity.isNotNull()) {
                    requestEnableBluetooth(activity!!)
                }
            }
        }
    }
    else{
        showSnackBar = true
        snackbartxt = "BLUETOOTH IS DISABLED"
        onStartClick = {
            if(activity.isNotNull()) {
                requestBluetoothPermissions(activity!!)
            }
        }
    }
    Box (
        Modifier
            .fillMaxSize()
            .padding(top = 85.dp),
        contentAlignment = Alignment.TopCenter){
        CustomSnackbar(
            message = snackbartxt,
            onStartClick = { onStartClick() },
            showSnackBar = showSnackBar
        )
    }


}