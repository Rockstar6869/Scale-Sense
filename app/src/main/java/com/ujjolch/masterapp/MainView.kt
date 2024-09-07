package com.ujjolch.masterapp

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.example.masterapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MainView(authViewModel: AuthViewModel,onLogOutSuccess:()->Unit,
             userDetailsViewModel: UserDetailsViewModel = viewModel()){
    val scaffoldstate: ScaffoldState = rememberScaffoldState()
    val vm:MainViewModel= viewModel()
    //to find out our current path
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentPath = navBackStackEntry?.destination?.route
    val currentScreen = remember {
        vm.currentScreen.value
    }
    val title = remember{
        mutableStateOf( currentScreen.title)
    }
    Scaffold(

        bottomBar = {
                BottomNavigation (modifier = Modifier.height(80.dp),
                    backgroundColor = Color.White){
                    listofbottomitems.forEach{
                        val isSelected = currentPath == it.broute
                        val TabTitle = if(it.title == "Home") R.string.Home
                                else if(it.title == "History") R.string.History
                                else R.string.Me
                        BottomNavigationItem(       //this is a inbuilt function
                            selected = currentScreen.route==it.broute,
                            onClick = { if(it.broute!=currentPath){
                                controller.navigate(it.broute)
                                vm.setCurrentScreen(it)
                                title.value=it.btitle}},
                            icon = {
                                val tint = if (isSelected) colorResource(id = R.color.Home_Screen_Blue) else Color.Black
                                    Icon(
                                        tint = tint,
                                        painter = painterResource(id = it.icon),
                                        contentDescription = it.btitle
                                    )
                            },
                            label = {Text(text = stringResource(id = TabTitle))})
                    }
                }
        },
        scaffoldState = scaffoldstate
    ){
        BottomScreenNavigation(navController = controller, pd = it, authViewModel = authViewModel,
            onLogOutSuccess = onLogOutSuccess,
            userDetailsViewModel = userDetailsViewModel)
    }

}