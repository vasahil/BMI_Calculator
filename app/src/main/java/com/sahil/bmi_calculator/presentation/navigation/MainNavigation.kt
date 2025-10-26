package com.sahil.bmi_calculator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.sahil.bmi_calculator.presentation.DetailScreen.DetailScreen
import com.sahil.bmi_calculator.presentation.HomeScreen.HomeScreen
import com.sahil.bmi_calculator.presentation.LoginScreen.LoginScreen
import com.sahil.bmi_calculator.presentation.ProfileScreen.ProfileScreen
import com.sahil.bmi_calculator.presentation.SignUpScreen.SignUpScreen

@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()

    val startScreen =
        if(firebaseAuth.currentUser == null) Routes.SingUp.route
    else Routes.Home.route

    NavHost(navController, startDestination = startScreen) {
        composable(Routes.Home.route) {
            HomeScreen(navController,firebaseAuth)
        }

        composable(Routes.SingUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Routes.DetailScreen.route) {
            DetailScreen(navController = navController)
        }

        composable(Routes.Login.route){
            LoginScreen(navController = navController)
        }

        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}