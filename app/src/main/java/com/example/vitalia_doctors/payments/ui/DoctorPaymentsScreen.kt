package com.example.vitalia_doctors.payments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- UI STATE MODELS ----------

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

// ---------- BOTTOM NAV ITEMS ----------

enum class PaymentsBottomNavItem(val label: String) {
    Home("Home"),
    Residents("Residents"),
    Emails("Emails"),
    Reports("Reports"),
    Settings("Settings")
}

// ---------- SCREEN ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPaymentsScreen(
    state: DoctorPaymentsUiState,
    selectedBottomItem: PaymentsBottomNavItem = PaymentsBottomNavItem.Residents,
    onBackClick: () -> Unit = {},
    onAddPaymentClick: () -> Unit = {},
    onBottomItemClick: (PaymentsBottomNavItem) -> Unit = {}
) {
    val backgroundColor = Color(0xFFF4F7FB)
    val primaryGreen = Color(0xFF22C55E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Finance",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPaymentClick,
                containerColor = primaryGreen
            ) {
                Text("+", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = selectedBottomItem == PaymentsBottomNavItem.Home,
                    onClick = { onBottomItemClick(PaymentsBottomNavItem.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == PaymentsBottomNavItem.Residents,
                    onClick = { onBottomItemClick(PaymentsBottomNavItem.Residents) },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Residents") },
                    label = { Text("Residents") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == PaymentsBottomNavItem.Emails,
                    onClick = { onBottomItemClick(PaymentsBottomNavItem.Emails) },
                    icon = { Icon(Icons.Default.Email, contentDescription = "Emails") },
                    label = { Text("Emails") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == PaymentsBottomNavItem.Reports,
                    onClick = { onBottomItemClick(PaymentsBottomNavItem.Reports) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Reports") },
                    label = { Text("Reports") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == PaymentsBottomNavItem.Settings,
                    onClick = { onBottomItemClick(PaymentsBottomNavItem.Settings) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --------- CARD: RESIDENT PAYMENTS ----------
            Text(
                text = "Resident Payments",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
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
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Amount Due",
                            style = MaterialTheme.typography.bodySmall,
                            color = primaryGreen,
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
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 96.dp) // espacio para el FAB
            ) {
                items(state.paymentHistory) { payment ->
                    PaymentHistoryCard(
                        item = payment,
                        primaryGreen = primaryGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentHistoryCard(
    item: PaymentHistoryItem,
    primaryGreen: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de pago
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(primaryGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Payment icon",
                    tint = primaryGreen
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
                    )
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
                )
            )
        }
    }
}

// ---------- PREVIEW ----------

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DoctorPaymentsScreenPreview() {
    val sampleState by remember {
        mutableStateOf(
            DoctorPaymentsUiState(
                residentName = "Eleanor Harper",
                amountDue = "$1,250.00",
                paymentHistory = listOf(
                    PaymentHistoryItem(
                        id = "1",
                        title = "Payment Received",
                        method = "Credit Card",
                        amount = "$1,250.00"
                    ),
                    PaymentHistoryItem(
                        id = "2",
                        title = "Payment Received",
                        method = "Credit Card",
                        amount = "$1,250.00"
                    ),
                    PaymentHistoryItem(
                        id = "3",
                        title = "Payment Received",
                        method = "Credit Card",
                        amount = "$1,250.00"
                    )
                )
            )
        )
    }

    DoctorPaymentsScreen(state = sampleState)
}
