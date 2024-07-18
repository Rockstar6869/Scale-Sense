package com.example.masterapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

//@Composable
//fun LogInScreen(onNavigateTosignin:()->Unit,
//                authViewModel: AuthViewModel,
//                onLogInSuccess:() -> Unit){
//    var email by remember { mutableStateOf("") }
//    var password by remember {
//        mutableStateOf("")
//    }
//    val result by authViewModel.authResult.observeAsState()
//    Column (verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier= Modifier
//            .fillMaxSize()
//            .padding(8.dp)){
//        OutlinedTextField(value = email,
//            onValueChange = {email=it},
//            label = { Text("Email") },
//            modifier = Modifier.padding(8.dp))
//        OutlinedTextField(value = password,
//            onValueChange = {password=it},
//            label = { Text("Password") },
//            modifier = Modifier.padding(8.dp))
//        Button(onClick = {
//            authViewModel.LogIn(email,password)
//            when(result){
//                is Result.Success->{
//                    onLogInSuccess()
//                }
//                is Result.Error ->{
//
//                }
//                else ->{}
//            }
//        }) {
//            Text(text = "Log In")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Don't have an account? Sign up.",
//            modifier = Modifier.clickable { onNavigateTosignin() }
//        )
//    }
//}

@Composable
fun LoginScreen(onNavigateTosignin:()->Unit, authViewModel: AuthViewModel, onLogInSuccess:() -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val result by authViewModel.authResult.observeAsState()
    var loginclick by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(loginclick) {
        if(loginclick){
            loading = true
            while(loading) {
                when (result) {
                    is Result.Success -> {
                        onLogInSuccess()
                        loading = false
                        loginclick = false
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "${(result as Result.Error).exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        loginclick = false
                        authViewModel.resetAuthResult()
                    }

                    else -> {}
                }
                delay(100)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    keyboardController?.hide()
                })
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.jipvi_logo1),
                contentDescription = "Company Logo",
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(40.dp))
            TransparentTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.LogIn(email,password)
                    loginclick = true
//                    when(result){
//                            is Result.Success->{
//                                onLogInSuccess()
//                            }
//                            is Result.Error ->{
//
//                            }
//                            else ->{}
//                        }

                          },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Customize the background color
                    contentColor = Color.White)
            ) {
                Text(
                    text = "Log In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
        }
            Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up.",
            color = Color.Blue,
            modifier = Modifier.clickable { onNavigateTosignin() }
        )
            Spacer(modifier = Modifier.height(16.dp))
            if(loading){
                LinearProgressIndicator()
            }
    }
}
}
