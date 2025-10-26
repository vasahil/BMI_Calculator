package com.sahil.bmi_calculator.presentation.navigation

sealed class Routes(val route:String) {
    object SingUp: Routes("singUp")
    object DetailScreen: Routes("details")
    object Login: Routes("login")
    object Home: Routes("Home")
    object Profile: Routes("profile")

}
