package com.ankur_anand.battery_charged_alarm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ankur_anand.battery_charged_alarm.ui.dashboard.DashboardScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.DashboardScreen.routName
    ){
        composable(
            route = Screen.DashboardScreen.routName
        ){
            DashboardScreen(navController = navController)
        }
    }
}