package com.ujjolch.masterapp

import android.util.Log
import android.widget.Toast
import com.example.masterapp.R
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun ChangePasswordScreen(
    authViewModel: AuthViewModel,
    onChangePasswordSuccess:()->Unit
) {
    var OldPassword by remember { mutableStateOf("") }
    var NewPassword by remember { mutableStateOf("") }
    var ConfirmPassword by remember { mutableStateOf("") }
    var changePasswordClick by remember {
        mutableStateOf(false)
    }
    var loading by remember { mutableStateOf(false) }
    val result by authViewModel.changePasswordResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(changePasswordClick) {
        if(changePasswordClick){
            loading = true
            while(loading) {
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(
                            context,
                            "Password Changed Successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onChangePasswordSuccess()
                        loading = false
                        changePasswordClick = false
                        authViewModel.resetChangePasswordResult()
                    }

                    is Result.Error -> {
                        val exceptionMessage = if( (((result as Result.Error).exception.message)?.startsWith("The supplied auth credential is incorrect"))?:false)
                            "Incorrect password"
                        else if( (((result as Result.Error).exception.message) == "The given password is invalid. [ Password should be at least 6 characters ]"))
                        "Password should contain at least 6 characters"
                        else (result as Result.Error).exception?.message
                        Log.d("changepassword","$exceptionMessage")
                        Toast.makeText(
                            context,
                            exceptionMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        changePasswordClick = false
                        authViewModel.resetChangePasswordResult()
                    }

                    else -> {}
                }
                delay(5)
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 70.dp)
                        , horizontalArrangement = Arrangement.Center){
                        androidx.compose.material.Text(text = stringResource(id = R.string.ChangePassword),
                            color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {onChangePasswordSuccess()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back")
                    }
                },
                backgroundColor = Color.White,
                modifier = Modifier
                    .height(80.dp)
                    .padding(top = 25.dp),
                elevation = 0.dp)
        },
        content = {
            Spacer(modifier = Modifier.padding(it))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                        })
                    },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight(0.7f)
                ) {
                    TransparentTextField(
                        value = OldPassword,
                        onValueChange = { OldPassword = it },
                        label = "Current Password"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))

                    TransparentTextField(
                        value = NewPassword,
                        onValueChange = { NewPassword = it },
                        label = "New Password",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(8.dp))

                    TransparentTextField(
                        value = ConfirmPassword,
                        onValueChange = { ConfirmPassword = it },
                        label = "Confirm Password",
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(40.dp))
                    if(loading){
                        LinearProgressIndicator()
                    }
                }


                Button(
                    onClick = {
                        if (OldPassword.isNotBlank() && NewPassword.isNotBlank() && ConfirmPassword.isNotBlank()) {
                            if(NewPassword==ConfirmPassword) {
                                if(OldPassword!=NewPassword) {
                                    authViewModel.changePassword(OldPassword, NewPassword)
                                    changePasswordClick = true
                                }
                                else{
                                    Toast.makeText(
                                        context,
                                        "Old password and new password cannot be same",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    "Password does not match",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(bottom = 100.dp)
                        .width(200.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black, // Customize the background color
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    )

}