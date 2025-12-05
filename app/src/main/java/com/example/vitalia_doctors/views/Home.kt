package com.example.vitalia_doctors.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.doctor.domain.repository.DoctorRepositoryImpl
import com.example.vitalia_doctors.doctor.ui.DoctorProfileScreen
import com.example.vitalia_doctors.doctor.ui.DoctorViewModel
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.payments.domain.repository.PaymentsRepositoryImpl
import com.example.vitalia_doctors.payments.ui.DoctorPaymentsScreen
import com.example.vitalia_doctors.payments.ui.PaymentsViewModel
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.views.nav.BottomNavItem

@Composable
fun Home(recordarPantalla: NavHostController, mainActivity: MainActivity) {
    val innerNavController = rememberNavController()

    // --- Repositories ---
    val doctorRepository = DoctorRepositoryImpl(RetrofitClient.doctorApiService)
    val paymentsRepository = PaymentsRepositoryImpl(RetrofitClient.receiptsApiService)

    // --- ViewModels (Single Instance for all tabs) ---
    val doctorViewModel = ViewModelProvider(
        mainActivity,
        DoctorViewModel.DoctorViewModelFactory(doctorRepository)
    ).get(DoctorViewModel::class.java)

    val paymentsViewModel = ViewModelProvider(
        mainActivity,
        PaymentsViewModel.Factory(paymentsRepository)
    ).get(PaymentsViewModel::class.java)

    // --- Bottom navigation items ---
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Payments,
        BottomNavItem.Notifications
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = innerNavController, items = items)
        },
        containerColor = LivelyOffWhite
    ) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Vista Home")
                }
            }
            composable(BottomNavItem.Profile.route) {
                DoctorProfileScreen(
                    navController = innerNavController,
                    viewModel = doctorViewModel
                )
            }
            composable(BottomNavItem.Payments.route) {
                DoctorPaymentsScreen(viewModel = paymentsViewModel)
            }
            composable(BottomNavItem.Notifications.route) {
                Notifications(mainActivity = mainActivity)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = LivelyDarkBlue,
        modifier = Modifier.height(78.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item) {
                            BottomNavItem.Home -> Icons.Filled.Home
                            BottomNavItem.Profile -> Icons.Filled.Person
                            BottomNavItem.Payments -> Icons.Filled.ShoppingCart
                            BottomNavItem.Notifications -> Icons.Filled.Notifications
                            BottomNavItem.Care -> TODO()
                            BottomNavItem.Residents -> TODO()
                        },
                        contentDescription = item.label,
                        tint = if (isSelected) LivelyGreen else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) LivelyGreen else Color.Gray
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White,
                    selectedIconColor = LivelyGreen,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = LivelyGreen,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
