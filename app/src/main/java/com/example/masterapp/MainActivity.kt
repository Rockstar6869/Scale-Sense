package com.example.masterapp

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

//import com.example.masterapp.ui.theme.MasterAppTheme

class MainActivity : ComponentActivity() {
    private var btpermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun scanBT(){  //I think this is permission checker
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
        if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
            }
        else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                bluetoothPermisssionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
//                bluetoothPermisssionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
            }
            else{
                bluetoothPermisssionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
        }


        enableEdgeToEdge()
        setContent {
            val bluetoothViewModel: BleScanViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val controller: NavController = rememberNavController()
            bluetoothViewModel.getPairedDevices()
                scanBT()
//                BleDeviceScreen( bleScanViewModel = bluetoothViewModel)
//                BmiCalculatorScreen(bleScanViewModel = bluetoothViewModel)
//                    AddDevice(bleScanViewModel = bluetoothViewModel)
//                            MainView()
            Navigation(navController = controller, authViewModel = authViewModel)
//           UpdateDetailScreen()
        }
    }
    private val bluetoothPermisssionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
            isGranted:Boolean->
        if(isGranted){
            val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btpermission = true
            if(bluetoothAdapter?.isEnabled == false){
                val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBTIntent)
            }
            else{

            }
        }
    }
    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result: ActivityResult ->
        if(result.resultCode == RESULT_OK){

        }
    }

//    private val leScanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            super.onScanResult(callbackType, result)
//            if (ContextCompat.checkSelfPermission(this@MainActivity, BLUETOOTH_CONNECT)
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                val device = result.device
//                val scanRecord = result.scanRecord
//                val deviceName = device.name
//                val deviceAddress = device.address
//                val advertisementData = scanRecord?.bytes
//
//                // Process the scanned device information
//                Log.d("RESULT","Device Name: $deviceName, Device Address: $deviceAddress")
//                advertisementData?.let {
//                    Log.d("RESULT","Advertisement Data: ${it.joinToString(", ")}")
//                }
//            }
//        }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MasterAppTheme {
//        Greeting("Android")
//    }
//}