package com.ankur_anand.battery_charged_alarm.navigation

const val DASHBOARD_SCREEN = "dashboard_screen"


sealed class Screen(val routName: String) {

    object DashboardScreen : Screen(routName = DASHBOARD_SCREEN)

}
