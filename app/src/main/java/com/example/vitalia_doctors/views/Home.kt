package com.example.vitalia_doctors.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.example.vitalia_doctors.model.beans.resident.Resident
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.payments.domain.repository.PaymentsRepositoryImpl
import com.example.vitalia_doctors.payments.ui.DoctorPaymentsScreen
import com.example.vitalia_doctors.payments.ui.PaymentsViewModel
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.views.home.DashboardUiState
import com.example.vitalia_doctors.views.home.HomeViewModel
import com.example.vitalia_doctors.views.nav.BottomNavItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Home(recordarPantalla: NavHostController, mainActivity: MainActivity) {
    val innerNavController = rememberNavController()
    val context = LocalContext.current

    // This effect checks for a one-time flag to redirect to the profile screen.
    LaunchedEffect(key1 = Unit) {
        val sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_login_pending", false)) {
            // Reset the flag so this doesn't happen again.
            sharedPreferences.edit().putBoolean("first_login_pending", false).apply()
            // Navigate to the profile screen.
            innerNavController.navigate(BottomNavItem.Profile.route) {
                // Pop the home screen from the back stack of the inner nav controller.
                popUpTo(BottomNavItem.Home.route) { inclusive = true }
            }
        }
    }

    // --- Repositories ---
    val doctorRepository = DoctorRepositoryImpl(RetrofitClient.doctorApiService)
    val paymentsRepository = PaymentsRepositoryImpl(RetrofitClient.receiptsApiService)

    // --- ViewModels (Single Instance for all tabs) ---
    val doctorViewModel = ViewModelProvider(mainActivity, DoctorViewModel.DoctorViewModelFactory(doctorRepository)).get(DoctorViewModel::class.java)
    val homeViewModel = ViewModelProvider(mainActivity, HomeViewModel.Factory(doctorRepository)).get(HomeViewModel::class.java)
    val paymentsViewModel = ViewModelProvider(mainActivity, PaymentsViewModel.Factory(paymentsRepository)).get(PaymentsViewModel::class.java)

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Payments, // Botón añadido
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
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(viewModel = homeViewModel)
            }
            composable(BottomNavItem.Payments.route) { // Ruta añadida
                DoctorPaymentsScreen(viewModel = paymentsViewModel)
            }
            composable(BottomNavItem.Profile.route) {
                DoctorProfileScreen(navController = innerNavController, viewModel = doctorViewModel)
            }
            composable(BottomNavItem.Notifications.route) {
                Notifications(mainActivity = mainActivity)
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
    val userFirstName = sharedPreferences.getString("firstName", null)

    LaunchedEffect(key1 = userFirstName) {
        if (userFirstName != null) {
            viewModel.loadDoctorData(userFirstName)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${uiState.error}", color = Color.Red)
        }
    } else {
        DashboardContent(uiState)
    }
}

@Composable
fun DashboardContent(uiState: DashboardUiState) {
    var selectedResident by remember { mutableStateOf<Resident?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard(doctorName = uiState.doctor?.fullName?.firstName ?: "Doctor")
        }

        if (uiState.assignedResidents.isNotEmpty()) {
            item {
                Text(
                    "Residentes Asignados",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(uiState.assignedResidents) { resident ->
                ResidentItem(resident = resident, onVerClick = { selectedResident = resident })
            }
        } else {
            item {
                Text("No tienes residentes asignados.", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }

    selectedResident?.let { resident ->
        ResidentDetailDialog(resident = resident, onDismiss = { selectedResident = null })
    }
}

@Composable
fun WelcomeCard(doctorName: String) {
    // Get current date
    val currentDate = LocalDate.now()
    // Spanish locale for month name
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = currentDate.format(formatter)

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = LivelyGreen)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar del cuidador",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp),
                tint = LivelyGreen
            )

            // Welcome Text and Date
            Column {
                Text(
                    text = "Dr. $doctorName",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}


@Composable
fun ResidentItem(resident: Resident, onVerClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar del residente",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LivelyGreen.copy(alpha = 0.1f))
                    .padding(8.dp),
                tint = LivelyGreen
            )
            Text(
                text = "${resident.firstName} ${resident.lastName}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onVerClick) {
                Text("Ver")
            }
        }
    }
}

@Composable
fun ResidentDetailDialog(resident: Resident, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Información del Residente",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Resident Details
                InfoRow("Nombre:", "${resident.firstName} ${resident.lastName}")
                InfoRow("DNI:", resident.dni)
                InfoRow("Género:", resident.gender)
                InfoRow("Fecha de Nacimiento:", resident.birthDate) // Idealmente, esto debería formatearse
                InfoRow("Ubicación:", "${resident.city}, ${resident.state}, ${resident.country}")

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(120.dp))
        Text(text = value)
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
                            else -> Icons.Filled.Home // Add a default case
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
