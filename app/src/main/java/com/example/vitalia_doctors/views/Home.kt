package com.example.vitalia_doctors.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.views.nav.BottomNavItem
import com.example.vitalia_doctors.views.nav.CareNavigation

// ---------- MODELOS PARA LA VISTA DE PAGOS ----------

data class DoctorPaymentsUiState(
    val residentName: String,
    val amountDue: String,
    val paymentHistory: List<PaymentHistoryItem>,
)

data class PaymentHistoryItem(
    val id: String,
    val title: String,
    val method: String,
    val amount: String,
)

// ---------------------------------------------------

@Composable
fun Home(recordarPantalla: NavHostController, mainActivity: MainActivity) {
    val innerNavController = rememberNavController()

    // Lista de items para la navegación inferior
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Care,
        BottomNavItem.Residents,
        BottomNavItem.Profile,
        BottomNavItem.Notifications
    )

    Scaffold(
        // La barra de navegación inferior
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
            // 👇 AQUÍ MOSTRAMOS LA VISTA DE PAYMENTS CUANDO SE ELIGE RESIDENTS
            composable(BottomNavItem.Residents.route) {
                DoctorPaymentsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LivelyOffWhite)
                        .padding(horizontal = 16.dp)
                )
            }
            composable(BottomNavItem.Profile.route) {
                // ProfileScreen()
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

//
// --------- CONTENIDO DE LA VISTA "FINANCE / RESIDENT PAYMENTS" ---------
//  (sin Scaffold; usa el Scaffold de Home)
//

@Composable
fun DoctorPaymentsContent(modifier: Modifier = Modifier) {
    val state = remember {
        DoctorPaymentsUiState(
            residentName = "Eleanor Harper",
            amountDue = "$1,250.00",
            paymentHistory = listOf(
                PaymentHistoryItem("1", "Payment Received", "Credit Card", "$1,250.00"),
                PaymentHistoryItem("2", "Payment Received", "Credit Card", "$1,250.00"),
                PaymentHistoryItem("3", "Payment Received", "Credit Card", "$1,250.00")
            )
        )
    }

    Column(
        modifier = modifier.padding(top = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Título superior
        Text(
            text = "Finance",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = LivelyDarkBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --------- CARD RESIDENT PAYMENTS ----------
        Text(
            text = "Resident Payments",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = LivelyDarkBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Resident: ${state.residentName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.amountDue,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = LivelyDarkBlue
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Amount Due",
                        style = MaterialTheme.typography.bodySmall,
                        color = LivelyGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFF0D0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Resident avatar",
                        tint = Color(0xFFE8793E),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --------- PAYMENT HISTORY ----------
        Text(
            text = "Payment History",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = LivelyDarkBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(state.paymentHistory) { payment ->
                PaymentHistoryCard(
                    item = payment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PaymentHistoryCard(
    item: PaymentHistoryItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LivelyGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite, // podrías cambiar a un ícono de pago
                    contentDescription = "Payment icon",
                    tint = LivelyGreen
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = LivelyDarkBlue
                )
                Text(
                    text = item.method,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Text(
                text = item.amount,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = LivelyDarkBlue
            )
        }
    }
}
