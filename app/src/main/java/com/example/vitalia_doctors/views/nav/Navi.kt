package com.example.vitalia_doctors.views.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.views.Home
import com.example.vitalia_doctors.views.LogIn
import com.example.vitalia_doctors.views.SignUp

@Composable
fun Navi(mainActivity: MainActivity) {
    val recordarPantalla = rememberNavController()

    NavHost(navController = recordarPantalla, startDestination = "LogIn") {
        composable("LogIn") { LogIn(recordarPantalla, mainActivity) }
        composable("SignUp") { SignUp(recordarPantalla) }
        composable("Home") { Home(recordarPantalla) }
    }
}