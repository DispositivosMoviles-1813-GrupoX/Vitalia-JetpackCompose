package com.example.vitalia_doctors.views.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
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
        composable("Home") { Home(recordarPantalla, mainActivity) }
        // La ruta 'doctor_profile' se gestionará dentro de la pantalla Home
    }
}

// Define las rutas y los iconos para la navegación inferior.
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Care : BottomNavItem("care", Icons.Default.Favorite, "Care")
    object Profile : BottomNavItem("doctor_profile", Icons.Default.Person, "Profile") // Ruta actualizada

    object Notifications: BottomNavItem("notifications", Icons.Default.Notifications, "Notifications")
}