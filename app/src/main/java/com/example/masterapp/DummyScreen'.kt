package com.example.masterapp

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DummyScreen() {
    var date by remember { mutableStateOf("") }
    var hist by remember {
        mutableStateOf<List<hist>>(emptyList())
    }
    var devices by remember {
        mutableStateOf<List<themistoscale>>(emptyList())
    }
    val UserDetailRepository: UserDetailRepository
    UserDetailRepository = UserDetailRepository(Injection.instance())
    var upload by remember {
        mutableStateOf(false)
    }
    val userRepository: UserRepository
        userRepository= UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    LaunchedEffect (upload){
        if(upload) {
            UserDetailRepository.updatehistlist("ro@gmail.com", hist(751.33, 40000, date))
            val data = UserDetailRepository.gethistlist("rohan@gmail.com")
            UserDetailRepository.bindDevice("rohan@gmail.com",themistoscale("Themisto body Scale","f"))
            val device = UserDetailRepository.getDevices("rohan@gmail.com")
            when(data){
                    is Result.Success ->{
                        hist = data.data
                    }
                is Result.Error ->{
                    Log.d("UJERROR","${data.exception}")
                }
            }
            when(device){
                is Result.Success ->{
                    devices = device.data
                }
                is Result.Error ->{
                    Log.d("UJERROR","${device.exception}")
                }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = {
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                Date()
            )
            date = currentDate
            upload = !upload

        }) {
            Text(text = "Show Date")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = date)
        Text(text = "${hist}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "$devices")
    }
}


@Composable
fun DummyScreen2(userDetailsViewModel: UserDetailsViewModel = viewModel()) {
    var date by remember { mutableStateOf("") }
    val hist by userDetailsViewModel.userHist.observeAsState()
    val devices by userDetailsViewModel.devices.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = {
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                Date()
            )
            date = currentDate
            userDetailsViewModel.bindDevice(themistoscale("Them","adress"))

        }) {
            Text(text = "Show Date")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = date)
        Text(text = "${hist}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "$hist")
        Text(text = "$devices")

    }
}