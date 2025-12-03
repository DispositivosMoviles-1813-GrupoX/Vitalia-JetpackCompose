package com.example.vitalia_doctors.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.doctor.data.remote.DoctorApiService
import com.example.vitalia_doctors.doctor.domain.repository.DoctorRepositoryImpl
import com.example.vitalia_doctors.doctor.ui.DoctorProfileScreen
import com.example.vitalia_doctors.doctor.ui.DoctorViewModel
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.views.nav.BottomNavItem
import com.example.vitalia_doctors.views.nav.CareNavigation

@Composable
fun Home(recordarPantalla: NavHostController, mainActivity: MainActivity) {
    val innerNavController = rememberNavController()

    // --- ViewModel Creation --- 
    // Se crea aquí para que persista entre pestañas de la navegación inferior.
    val doctorApiService = RetrofitClient.doctorApiService
    val doctorRepository = DoctorRepositoryImpl(doctorApiService)
    val factory = DoctorViewModel.DoctorViewModelFactory(doctorRepository)
    val doctorViewModel = ViewModelProvider(mainActivity, factory).get(DoctorViewModel::class.java)

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Care,
        BottomNavItem.Profile,
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
            Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeContent(Modifier.fillMaxSize())
            }
            composable(BottomNavItem.Care.route) {
                CareNavigation()
            }
            composable(BottomNavItem.Profile.route) {
                // Ahora solo pasamos el ViewModel que ya fue creado.
                DoctorProfileScreen(navController = innerNavController, viewModel = doctorViewModel)
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
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) LivelyGreen else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) LivelyGreen else Color.Gray,
                        fontSize = 10.sp
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LivelyOffWhite)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Lively",
            color = LivelyGreen,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Peace of mind for you and your loved ones",
            color = LivelyDarkBlue,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Lively connects family, caregivers, and doctors to monitor and support senior residents, ensuring their safety and well-being.",
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))
        Text(
            text = "Our Services",
            color = LivelyDarkBlue,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(24.dp))

        ServiceCard(label = "Care Management", description = "Personalized care plans and coordination.")
        ServiceCard(label = "Home Monitoring", description = "Real-time alerts and activity tracking.")
        ServiceCard(label = "Family Collaboration", description = "Shared access and communication tools.")
        ServiceCard(label = "24/7 Support", description = "Immediate assistance whenever needed.")

        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { /* Acción del botón */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(text = "Contact Us", fontSize = 18.sp, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun ServiceCard(label: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = LivelyGreen,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = LivelyDarkBlue
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}