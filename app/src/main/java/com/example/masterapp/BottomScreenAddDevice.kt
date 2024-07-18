package com.example.masterapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomScreenAddDevice(bleScanViewModel: BleScanViewModel = viewModel()
,onNavigatetoMyDevice:()->Unit){
    AddDevice(bleScanViewModel = bleScanViewModel) {
            onNavigatetoMyDevice()
    }
}