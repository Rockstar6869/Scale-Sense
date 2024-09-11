package com.ujjolch.masterapp
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import com.example.masterapp.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun MyDeviceScreen(onNavBackClicked:()->Unit,
                   onAddDeviceClicked:()->Unit,
                   userDetailsViewModel: UserDetailsViewModel = viewModel()
) {
    val devices by userDetailsViewModel.devices.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(true) {
        delay(1000)
        userDetailsViewModel.getDeviceList()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.MyDevices)) },
                navigationIcon = {
                    IconButton(onClick = { onNavBackClicked() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if(hasBluetoothPermissions(context) && hasLocationPermissions(context)) {
                            onAddDeviceClicked()
                        }
                        else{
                            ToastManager.showToast(context,"Permissions not granted",Toast.LENGTH_SHORT)
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Device")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 8.dp
            )
        },
        content = {
            Spacer(modifier = Modifier.padding(it))
            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(devices?: emptyList()) { 
                DeviceInfoRow(name = it.name, address = it.address)
            }
        }
            
        }
    )
}


@Composable
fun DeviceInfoRow(name: String, address: String) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .fillMaxWidth()
        ) {

            Text(
                text = name,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}