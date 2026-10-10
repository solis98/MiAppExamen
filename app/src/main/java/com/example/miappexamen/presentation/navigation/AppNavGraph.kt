package com.example.miappexamen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miappexamen.presentation.auth.LoginScreen
import com.example.miappexamen.presentation.auth.RegisterScreen
import com.example.miappexamen.presentation.home.HomeScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier

@Composable
fun AppNavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login") {
            Box(
                modifier = Modifier.safeDrawingPadding()
            ) {
                LoginScreen(navController)
            }
        }

        composable("register") {
            Box(
                modifier = Modifier.safeDrawingPadding()
            ) {
                RegisterScreen(navController)
            }
        }

        composable("home") {
            DrawerScaffold(navController) {
                HomeScreen()
            }
        }
    }
}