package com.ujjolch.masterapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.example.masterapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun MyProfileScreen(onMyDevicesClick:()->Unit,
                    onUpdateDetailsClick:()->Unit,authViewModel: AuthViewModel,
                    onChangePasswordClick:()->Unit,
                    onUnitClick:()->Unit,
                    onLogOutSuccess:()->Unit,
                    onPrivacyPolicyClick:()->Unit,
                    onUserManagementClick:()->Unit,
                    onLanguageClick:()->Unit,
                    userDetailsViewModel: UserDetailsViewModel = viewModel()
) {
    var logoutclick by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val result by authViewModel.authResult.observeAsState()
    val context = LocalContext.current
    val userdata by userDetailsViewModel.currentUser.observeAsState()
    LaunchedEffect(true) {
        userDetailsViewModel.getCurrentSubUserData()
    }
    LaunchedEffect(logoutclick) {
        if (logoutclick) {
            loading = true
            while (loading) {
                when (result) {
                    is Result.Success -> {
                        onLogOutSuccess()
                        loading = false
                        logoutclick = false
                        authViewModel.resetAuthResult()
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "${(result as Result.Error).exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        loading = false
                        logoutclick = false
                        authViewModel.resetAuthResult()
                    }

                    else -> {}
                }
                delay(100)
            }
        }
    }
    LazyColumn {
        item {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {

        Spacer(modifier = Modifier.padding(vertical = 40.dp))
        Text(
            text = stringResource(id = R.string.Settings),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        if (userdata != null) {
            UserInfoRow(
                Email = userdata!!.email,
                firstname = userdata!!.firstName,
                lastname = userdata!!.lastName
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 40.dp))
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(Icon = painterResource(id = ScreenInMeScreen.UserManagement.icon), textContent = {
            Text(text = stringResource(id = R.string.UserManagement))
        }) {
            onUserManagementClick()
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        ClickableRow(Icon = painterResource(id = ScreenInMeScreen.MyDevices.icon), textContent = {
            Text(text = stringResource(id = R.string.MyDevices))
        }) {
            onMyDevicesClick()
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        ClickableRow(
            Icon = painterResource(id = ScreenInMeScreen.UpdateDetails.icon),
            textContent = { Text(text = stringResource(id = R.string.updateDetails)) }) {
            onUpdateDetailsClick()
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(Icon = painterResource(id = ScreenInMeScreen.Unit.icon), textContent = {
            Text(text = stringResource(id = R.string.units))
        }) {
            onUnitClick()
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        Spacer(modifier = Modifier.padding(vertical = 40.dp))
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(Icon = painterResource(id = ScreenInMeScreen.SelectLanguageScreen.icon), textContent = {
            Text(text = stringResource(id = R.string.Language))
        }) {
            onLanguageClick()
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        Spacer(modifier = Modifier.padding(vertical = 40.dp))
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(Icon =  painterResource(id = R.drawable.privacy_policy_icon), textContent = { 
            Text(text = stringResource(id = R.string.PrivacyPolicy))
        }) {
            onPrivacyPolicyClick()
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(Icon = painterResource(id = R.drawable.baseline_password_24), textContent = {
            Text(text = stringResource(id = R.string.ChangePassword))
        }) {
            onChangePasswordClick()
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)
        ClickableRow(
            Icon = painterResource(id = R.drawable.baseline_logout_24), textContent = {
                Text(
                    text = stringResource(id = R.string.LogOut),
                    color = Color.Red
                )
            },
            tint = Color.Red
        ) {
            authViewModel.LogOut()
            userDetailsViewModel.clearUserDataAndHist()
            logoutclick = true
        }
        Divider(color = Color.LightGray, thickness = 0.9.dp)

        Spacer(modifier = Modifier.padding(vertical = 50.dp))
        if (loading) {
            LinearProgressIndicator()
        }
    }
}
}
    }






@Composable
fun ClickableRow(
    Icon: Painter,
    tint:Color = Color.Black,
    textContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = Icon,
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            tint = tint)
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            textContent()
        }
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun UserInfoRow(Email: String, firstname: String,lastname:String) {
    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .fillMaxWidth()
        ) {

            Text(
                text = Email,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "$firstname ",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = lastname,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun MyProfileScreenPreview(){
//    MyProfileScreen(onMyDevicesClick = { /*TODO*/ }, onUpdateDetailsClick = { /*TODO*/ })
//}