package com.ujjolch.masterapp

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.masterapp.R
import kotlinx.coroutines.delay

@Composable
fun AddDeviceCheckerScreen(onNavigateToAddDeviceScreen:()->Unit,
                           onNavigateToMeScreen:()->Unit,
                           backButtonEnabled:Boolean) {
    val context = LocalContext.current
    var targetprogressbt by remember { mutableStateOf(0f) }
    var progressbt by remember { mutableStateOf(0f) }

    var targetprogressloc by remember { mutableStateOf(0f) }
    var progressloc by remember { mutableStateOf(0f) }

    var targetprogressnet by remember { mutableStateOf(0f) }
    var progressnet by remember { mutableStateOf(0f) }

    var buttonClick by remember { mutableStateOf(0) }

    var checkBTAndRestart by remember { mutableStateOf(0) }
    var checkLocAndRestart by remember { mutableStateOf(0) }
    var checkNetAndRestart by remember { mutableStateOf(0) }


    var isBluetoothEnabled by remember { mutableStateOf(false) }
    var isBluetoothEnabled2 by remember { mutableStateOf(false) }

    var isLocEnabled by remember { mutableStateOf(false) }
    var isLocEnabled2 by remember { mutableStateOf(false) }

    var isNetEnabled by remember { mutableStateOf(false) }
    var isNetEnabled2 by remember { mutableStateOf(false) }


    // Animate progress from 0 to 1
    val animatedProgressbt by animateFloatAsState(
        targetValue = progressbt,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    )

    val animatedProgressLoc by animateFloatAsState(
        targetValue = progressloc,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    )

    val animatedProgressNet by animateFloatAsState(
        targetValue = progressnet,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    )

    LaunchedEffect(buttonClick) {
        // Reset progress to 0 before starting the animation
        progressbt = 0f
        progressloc = 0f
        progressnet = 0f
        delay(800) // A small delay to ensure reset is visible

        if (isBluetoothEnabled(context)) {
            isBluetoothEnabled = true
        } else {
            isBluetoothEnabled = false

        }

        // Start animation to 1f
        progressbt = 1f
    }
    LaunchedEffect(Unit) {
        while(true){
            isBluetoothEnabled2 = isBluetoothEnabled(context)
            isLocEnabled2 = isLocationEnabled(context)
            isNetEnabled2 = isInternetAvailable(context)
            delay(1000)
        }
    }
    LaunchedEffect(checkBTAndRestart) {
        if(checkBTAndRestart>0) {
            while(true) {
                if (isBluetoothEnabled2) {
                    buttonClick++
                    checkBTAndRestart= 0
                }
                delay(1000)
            }
        }
    }
    LaunchedEffect(checkLocAndRestart) {
        if(checkLocAndRestart>0) {
            while(true) {
                if (isLocEnabled2) {
                    buttonClick++
                    checkLocAndRestart= 0
                }
                delay(1000)
            }
        }
    }
    LaunchedEffect(checkNetAndRestart) {
        if(checkNetAndRestart>0) {
            while(true) {
                if (isNetEnabled2) {
                    buttonClick++
                    checkNetAndRestart= 0
                }
                delay(1000)
            }
        }
    }
    LaunchedEffect(animatedProgressbt) {
        if (animatedProgressbt == 1f && isBluetoothEnabled == false) {
            (context as? Activity)?.let { activity ->
                requestEnableBluetooth(activity)
                checkBTAndRestart++
            }
        }
        if (isLocationEnabled(context)) {
            isLocEnabled = true
        } else {
            isLocEnabled = false
        }
        if (animatedProgressbt == 1f && isBluetoothEnabled == true) {
            progressloc = 1f
        }
    }
    LaunchedEffect(animatedProgressLoc) {
        if (animatedProgressLoc == 1f && isLocEnabled == false) {
            (context as? Activity)?.let { activity ->
                requestEnableLocation(activity)
                checkLocAndRestart++
            }
        }
        isNetEnabled = isInternetAvailable(context)
        if (animatedProgressLoc == 1f && isLocEnabled == true) {
            progressnet = 1f
        }
    }

    LaunchedEffect(animatedProgressNet) {
        if (animatedProgressNet == 1f && !isNetEnabled) {
            (context as? Activity)?.let { activity ->
                openWiFiSettings(activity)
                checkNetAndRestart++
            }
        }
    }
    if (targetprogressbt > progressbt) {
        targetprogressbt = progressbt
    } else if (progressbt > targetprogressbt) {
        targetprogressbt = animatedProgressbt
    }


    if (targetprogressloc > progressloc) {
        targetprogressloc = progressloc
    } else if (targetprogressloc < progressloc) {
        targetprogressloc = animatedProgressLoc
    }


    if (targetprogressnet > progressnet) {
        targetprogressnet = progressnet
    } else if (targetprogressnet < progressnet) {
        targetprogressnet = animatedProgressNet
    }
    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Row(
                        Modifier
                            .fillMaxWidth()
//                            .padding(end = 70.dp)
                        ,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.checkPhoneStatus),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = {
                       onNavigateToMeScreen()
                    },
                        enabled = backButtonEnabled) {
                       Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                           tint = if(backButtonEnabled) Color.Black else Color.White
                        )
                    }
                },
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        buttonClick++
                    }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.Refresh, // Replace with the desired icon
                            contentDescription = "Refresh"
                        )
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp
            )
        },
        content = {
    Box(
        Modifier
            .fillMaxSize()
            .padding(it)
            .padding(horizontal = 30.dp)
            .padding(top = 30.dp)
            .padding(bottom = 85.dp)
    ) {
        Column(Modifier.align(Alignment.TopCenter)) {
            Text(text = "Bluetooth")
//            Text(text = "$isBluetoothEnabled")
            StatusCheckerIndicator(progress = targetprogressbt, isEnabled = isBluetoothEnabled)
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Location")
            StatusCheckerIndicator(progress = targetprogressloc, isEnabled = isLocEnabled)
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Network")
            StatusCheckerIndicator(progress = targetprogressnet, isEnabled = isNetEnabled)
        }
        Column(Modifier.align(Alignment.BottomCenter)) {
            Button(
                onClick = {
                        onNavigateToAddDeviceScreen()
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = (isBluetoothEnabled && isLocEnabled && isNetEnabled && (animatedProgressbt == 1f && animatedProgressLoc ==1f && animatedProgressNet==1f)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White
                )
            )
            {
                androidx.compose.material3.Text(
                    text = stringResource(id = R.string.Continue),
                    color = Color.White
                )
            }
        }
    }
}
)
}




@Composable
fun StatusCheckerIndicator(
    progress: Float,
    isEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Linear Progress Indicator
        LinearProgressIndicator(
            progress = progress,
            backgroundColor = colorResource(id = R.color.Icon_Tint),
            color = if(progress == 1f && !isEnabled) Color.Red else colorResource(id = R.color.Slider_DarkGreen),
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        )


        Icon(
            painter = painterResource(id = R.drawable.baseline_check_circle_24),
            contentDescription = "Check Icon",
            tint = if (progress==1f && isEnabled) colorResource(id = R.color.Slider_DarkGreen) else colorResource(id = R.color.Icon_Tint)
        )
    }
}