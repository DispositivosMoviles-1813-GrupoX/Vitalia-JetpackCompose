package com.example.vitalia_doctors.payments.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPaymentsScreen(
    viewModel: PaymentsViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Payments") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        tint = LivelyGreen
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = LivelyDarkBlue
                )
            )
        },
        containerColor = LivelyOffWhite
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = LivelyGreen
                    )
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No pudimos cargar tus pagos.",
                            color = LivelyDarkBlue,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.error!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadReceiptsOLD() }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    PaymentsContent(
                        state = state,
                        onResidentSelected = { id -> viewModel.selectResident(id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentsContent(
    state: DoctorPaymentsUiState,
    onResidentSelected: (Long?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SummaryCard(summary = state.summary)

        Spacer(Modifier.height(16.dp))

        if (state.residents.isNotEmpty()) {
            ResidentFilterRow(
                residents = state.residents,
                selectedResidentId = state.selectedResidentId,
                onResidentSelected = onResidentSelected
            )
            Spacer(Modifier.height(12.dp))
        }

        PremiumUpsellCard()

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Payment history",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = LivelyDarkBlue
        )

        Spacer(Modifier.height(8.dp))

        if (state.filteredReceipts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay pagos registrados para este doctor.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.filteredReceipts) { item ->
                    PaymentListItem(item = item)
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(summary: PaymentsSummary) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = LivelyDarkBlue
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "To collect",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = summary.totalToCollectLabel,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = LivelyDarkBlue
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Collected",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = summary.totalCollectedLabel,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = LivelyGreen
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${summary.pendingCount} pagos pendientes",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ResidentFilterRow(
    residents: List<Long>,
    selectedResidentId: Long?,
    onResidentSelected: (Long?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedResidentId == null,
            onClick = { onResidentSelected(null) },
            label = { Text("Todos") }
        )

        residents.forEach { id ->
            FilterChip(
                selected = selectedResidentId == id,
                onClick = { onResidentSelected(id) },
                label = { Text("Resident #$id") }
            )
        }
    }
}

@Composable
private fun PremiumUpsellCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = LivelyDarkBlue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Vitalia Premium",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Próximamente: reportes avanzados, facturación automática y soporte prioritario.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* TODO: integrar con billing / web */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Coming soon")
            }
        }
    }
}

@Composable
private fun PaymentListItem(item: PaymentHistoryItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = item.residentLabel,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = LivelyDarkBlue
                )
                Text(
                    text = item.statusLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (item.statusLabel == "Paid") LivelyGreen else Color(0xFFFFA000)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.totalAmountLabel,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = LivelyDarkBlue
                )
                Text(
                    text = "Pagado: ${item.amountPaidLabel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${item.issueDateLabel} · due ${item.dueDateLabel}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = item.paymentMethodLabel,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DoctorPaymentsScreenPreview() {
    val sampleState = DoctorPaymentsUiState(
        receipts = listOf(
            PaymentHistoryItem(
                receiptId = 1,
                residentId = 10,
                residentLabel = "Resident #10",
                statusLabel = "Paid",
                issueDateLabel = "04 Dec 2025",
                dueDateLabel = "10 Dec 2025",
                totalAmountLabel = "$150.00",
                amountPaidLabel = "$150.00",
                paymentMethodLabel = "Card"
            )
        ),
        summary = PaymentsSummary(
            totalToCollectLabel = "$0.00",
            totalCollectedLabel = "$150.00",
            pendingCount = 0
        )
    )

    MaterialTheme {
        PaymentsContent(
            state = sampleState,
            onResidentSelected = {}
        )
    }
}
