package com.example.masterapp

import androidx.annotation.DrawableRes

sealed class Screen(val title:String,val route:String){
    sealed class BottomScreen(val btitle:String,val broute:String,@DrawableRes val icon:Int):Screen(btitle,broute){
        object HomeScreen:BottomScreen("Home","Home",R.drawable.baseline_home_24)
        object HistoryScreen:BottomScreen("History","History",R.drawable.baseline_auto_graph_24)
        object MeScreen:BottomScreen("Me","Me",R.drawable.baseline_manage_accounts_24)
    }
    object IntroScreen:Screen("Intro Screen","introscreen")
    object SignInScreen:Screen("Sign In Screen","signin")
    object LogInScreen:Screen("Log In Screen","login")
    object AddDeviceScreen:Screen("Add Device Screen","adddevice")
    object UpdateDetailScreen:Screen("Update Detail Screen","updatedetail")
    object MainView:Screen("MainView","mainview")
    object BottomScreenAddDevice:Screen("Add Device","bottomadddevice")
    object BottomScreenUpdateDetails:Screen("Update Details","bottomscreenupdatedetails")
    object HealthReportScreen:Screen("Health Report","healthreport")


}
sealed class ScreenInMeScreen(val atitle:String,val aroute:String,@DrawableRes val icon:Int):Screen(atitle, aroute){
    object MyDevices:ScreenInMeScreen("My Devices","mydevices",R.drawable.baseline_monitor_weight_24)
    object UpdateDetails:ScreenInMeScreen("Update Details","updatedetails",R.drawable.baseline_update_24)
}



val listofbottomitems = listOf(Screen.BottomScreen.HomeScreen,
    Screen.BottomScreen.HistoryScreen,
    Screen.BottomScreen.MeScreen)

//val listofmescreenitems = listOf(ScreenInMeScreen.MyDevices,
//    ScreenInMeScreen.UpdateDetails)