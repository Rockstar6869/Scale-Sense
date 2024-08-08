package com.example.masterapp

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomScreenAddDevice(bleScanViewModel: BleScanViewModel = viewModel(),
    userDetailsViewModel: UserDetailsViewModel
,onNavigatetoMyDevice:()->Unit){
//    AddDevice(bleScanViewModel = bleScanViewModel) {
//            onNavigatetoMyDevice()
//    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Device") },
                navigationIcon = {
                    IconButton(onClick = { onNavigatetoMyDevice()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 8.dp)
        },
        content = {
            AddDevice(bleScanViewModel = bleScanViewModel) {
                userDetailsViewModel.getDeviceList()
            onNavigatetoMyDevice()
    }
        }
    )
}