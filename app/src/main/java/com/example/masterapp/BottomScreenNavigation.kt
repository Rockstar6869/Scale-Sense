package com.example.masterapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun BottomScreenNavigation(navController: NavController,pd: PaddingValues,
                           bluetoothViewModel: BleScanViewModel = viewModel(),
                           userDetailsViewModel: UserDetailsViewModel = viewModel(),
        authViewModel: AuthViewModel , onLogOutSuccess:()->Unit){
    var backPressedTime by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(true){
        withContext(Dispatchers.IO) {
            userDetailsViewModel.gethistlist()
            userDetailsViewModel.getUnits()
        }
    }
    NavHost(navController = navController as NavHostController,
        startDestination = Screen.BottomScreen.HomeScreen.route ){

        composable(Screen.BottomScreen.HomeScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {       BackHandler {

            handleBackButtonPress(
                context = context,
                backPressedTime = backPressedTime,
                onUpdateTime = { backPressedTime = it },
                onExit = { (navController.context as? Activity)?.finish() }
            )

        }

                HomeScreen( bleScanViewModel =  bluetoothViewModel,
                    onNavigateToHealthReport = {navController.navigate(Screen.HealthReportScreen.route)},
                    userDetailsViewModel = userDetailsViewModel)
        }
        composable(Screen.BottomScreen.HistoryScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {
            BackHandler {  //used for handling back button click
                handleBackButtonPress(   //used for exiting if back is pressed twice
                    context = context,
                    backPressedTime = backPressedTime,
                    onUpdateTime = { backPressedTime = it },
                    onExit = { (navController.context as? Activity)?.finish() }
                )
            }
            HistoryScreen(userDetailsViewModel)
        }
        composable(Screen.BottomScreen.MeScreen.route,
//            enterTransition =  {slideInHorizontally() }
        )
        {
            BackHandler {  //used for handling back button click
                handleBackButtonPress(   //used for exiting if back is pressed twice
                    context = context,
                    backPressedTime = backPressedTime,
                    onUpdateTime = { backPressedTime = it },
                    onExit = { (navController.context as? Activity)?.finish() }
                )
            }
            MyProfileScreen(onMyDevicesClick = {navController.navigate(ScreenInMeScreen.MyDevices.route)},
                            onUpdateDetailsClick = {navController.navigate(Screen.BottomScreenUpdateDetails.route)},
                            onChangePasswordClick = {navController.navigate(ScreenInMeScreen.ChangePassword.route)},
                            onUnitClick = {navController.navigate(ScreenInMeScreen.Unit.route)},
                            onLogOutSuccess = onLogOutSuccess,
                            authViewModel = authViewModel,
                            userDetailsViewModel = userDetailsViewModel)
        }
        composable(ScreenInMeScreen.MyDevices.route){
            MyDeviceScreen(onNavBackClicked = {navController.navigate(Screen.BottomScreen.MeScreen.route) },
                onAddDeviceClicked = {navController.navigate(Screen.BottomScreenAddDevice.route)},
                userDetailsViewModel = userDetailsViewModel)
        }
        composable(Screen.BottomScreenAddDevice.route){
            BottomScreenAddDevice(userDetailsViewModel = userDetailsViewModel) {
                navController.navigate(ScreenInMeScreen.MyDevices.route)
            }
        }
        composable(Screen.BottomScreenUpdateDetails.route){
            BottomScreenUpdateDetails (userDetailsViewModel = userDetailsViewModel,
                onNavigateToMeScreen = { navController.navigate(Screen.BottomScreen.MeScreen.route)
                    userDetailsViewModel.updateUserData()})
        }
        composable(Screen.HealthReportScreen.route){
            HealthReportScreen(onBackClick = { navController.navigate(Screen.BottomScreen.HomeScreen.route) },
                userDetailsViewModel = userDetailsViewModel)
        }
        composable(ScreenInMeScreen.ChangePassword.route){
            ChangePasswordScreen(authViewModel = authViewModel,
                                onChangePasswordSuccess = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
        composable(ScreenInMeScreen.Unit.route){
            UnitScreen(userDetailsViewModel = userDetailsViewModel,
                onNavigateToMeScreen = {navController.navigate(Screen.BottomScreen.MeScreen.route)})
        }
    }

}

fun handleBackButtonPress(
    context: Context,
    backPressedTime: Long,
    onUpdateTime: (Long) -> Unit,
    onExit: () -> Unit
) {
    val currentTime = System.currentTimeMillis()
    if (currentTime - backPressedTime < 2000) {
        onExit()
    } else {
        onUpdateTime(currentTime)
        Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
    }
}