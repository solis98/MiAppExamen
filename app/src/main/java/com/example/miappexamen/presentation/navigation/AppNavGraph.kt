package com.example.miappexamen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miappexamen.presentation.auth.LoginScreen
import com.example.miappexamen.presentation.auth.RegisterScreen

@Composable
fun AppNavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){
            LoginScreen(navController)
        }

        composable("register"){
            RegisterScreen(navController)
        }
    }
}