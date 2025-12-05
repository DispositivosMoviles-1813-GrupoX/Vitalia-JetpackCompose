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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.model.beans.notifications.NotificationResponse
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.viewmodel.NotificationsViewModel

// Nuevos colores para estados de notificación
val UnreadBackground = Color(0xFFE8F5E9) // Verde muy pálido (similar al que ya usabas)
val ReadBackground = Color.White
val ArchivedBackground = Color(0xFFEEEEEE) // Gris claro para archivado
val UnarchivedBackground = Color(0xFFFFFBE0) // Amarillo pálido para desarchivado/pendiente

// 1. Definición de la lista de filtros
val filterOptions = listOf("ALL", "UNREAD", "READ", "ARCHIVED", "UNARCHIVED")

@Composable
fun Notifications(viewModel: NotificationsViewModel = viewModel(), mainActivity: MainActivity) {

    val notifications = viewModel.notificationsList
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    var selectedFilter by remember { mutableStateOf("ALL") }

    val currentUserId by remember {
        val pref = mainActivity.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        mutableLongStateOf(pref.getLong("userId", 0L))
    }

    val loadNotifications: (String) -> Unit = { status ->
        if (currentUserId > 0L) {
            when (status) {
                "ALL" -> viewModel.getNotificationsByUserId(currentUserId)
                else -> viewModel.getNotificationsByUserIdAndStatus(currentUserId, status)
            }
        } else {
            viewModel.errorMessage = "No se encontró el ID de usuario. Inicie sesión nuevamente."
        }
    }

    LaunchedEffect(selectedFilter) {
        loadNotifications(selectedFilter)
    }

    // ------------------
    // CONTENEDOR PRINCIPAL CON FAB
    // ------------------
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LivelyOffWhite)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Notificaciones",
                style = MaterialTheme.typography.headlineLarge,
                color = LivelyDarkBlue,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FilterButtonsRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter -> selectedFilter = filter }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = LivelyGreen
                )

                errorMessage != null -> Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                notifications.isEmpty() -> Text(
                    text = "No hay notificaciones para mostrar.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(notifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                viewModel = viewModel,
                                onActionSuccess = { loadNotifications(selectedFilter) }
                            )
                        }
                    }
                }
            }
        }

        // <-- Aquí invocamos el botón con el diálogo integrado
        FloatingCreateAlertButton(
            viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        )
    }
}


@Composable
fun FilterButtonsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filterOptions) { filter ->
            val isSelected = filter == selectedFilter
            Button(
                onClick = { onFilterSelected(filter) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) LivelyGreen else Color(0xFFE0E0E0), // Gris más limpio
                    contentColor = if (isSelected) Color.White else LivelyDarkBlue
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp) // Más padding
            ) {
                Text(filter.capitalize(), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Componente Composable para mostrar una sola notificación en una tarjeta simple.
 */
@Composable
fun NotificationCard(
    notification: NotificationResponse,
    viewModel: NotificationsViewModel, // Añadido el ViewModel
    onActionSuccess: () -> Unit // Añadida la función para refrescar la lista
) {
    var showDialog by remember { mutableStateOf(false) }

    // Definición de colores distintivos
    val indicatorColor: Color
    val backgroundColor: Color
    val statusLabel: String

    when (notification.status) {
        "UNREAD" -> {
            indicatorColor = LivelyGreen
            backgroundColor = UnreadBackground // Usamos el verde pálido
            statusLabel = "Nuevo"
        }
        "READ" -> {
            indicatorColor = Color.LightGray
            backgroundColor = ReadBackground
            statusLabel = "Leído"
        }
        "ARCHIVED" -> {
            indicatorColor = Color.DarkGray
            backgroundColor = ArchivedBackground // Gris claro
            statusLabel = "Archivado"
        }
        "UNARCHIVED" -> {
            indicatorColor = Color(0xFFFF9800) // Naranja
            backgroundColor = UnarchivedBackground // Amarillo pálido
            statusLabel = "Activo"
        }
        else -> {
            indicatorColor = Color.Gray
            backgroundColor = ReadBackground
            statusLabel = "Desconocido"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp), // Padding vertical más ajustado
        shape = RoundedCornerShape(12.dp), // Esquinas más redondeadas
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // Color de fondo basado en el estado
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Sombra un poco más visible
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Más padding interno
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Indicador visual (Pequeño círculo de color)
            Spacer(
                modifier = Modifier
                    .size(10.dp)
                    .background(indicatorColor, MaterialTheme.shapes.extraSmall) // Círculo de color
            )
            Spacer(modifier = Modifier.width(12.dp))

            // 2. Título de la notificación (Ocupa el resto del espacio)
            Text(
                text = notification.title,
                fontWeight = if (notification.status == "UNREAD") FontWeight.ExtraBold else FontWeight.SemiBold, // Hacemos el no leído más fuerte
                color = LivelyDarkBlue,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            // 3. Etiqueta de estado
            Text(
                text = statusLabel,
                color = indicatorColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 4. Botón para mostrar el detalle (Estilo más ligero)
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF0F0F0), // Fondo muy claro
                    contentColor = LivelyDarkBlue
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Sin sombra
            ) {
                Text("Ver", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    // Muestra el diálogo si showDialog es true
    if (showDialog) {
        NotificationDetailDialog(
            notification = notification,
            viewModel = viewModel, // Pasando el ViewModel al diálogo
            onDismiss = { showDialog = false },
            onActionSuccess = {
                showDialog = false
                onActionSuccess() // Ejecuta la función de refresco
            }
        )
    }
}

/**
 * Componente Diálogo para mostrar la información completa de la notificación.
 */
@Composable
fun NotificationDetailDialog(
    notification: NotificationResponse,
    viewModel: NotificationsViewModel,
    onDismiss: () -> Unit,
    onActionSuccess: () -> Unit // Callback para ejecutar después de la acción
) {
    val isArchived = notification.status == "ARCHIVED"
    val isRead = notification.status == "READ"

    // Asumimos que la acción "Marcar Leído" revierte el estado Archivada.
    val readButtonText = if (isArchived) "Desarchivar (Leído)" else "Marcar Leído"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp), // Más redondeado
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp), // Más padding
                horizontalAlignment = Alignment.Start
            ) {
                // Encabezado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle de Notificación",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = LivelyDarkBlue
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Título de la Notificación
                Text(
                    text = "Título:",
                    fontWeight = FontWeight.SemiBold,
                    color = LivelyGreen,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = notification.title,
                    color = LivelyDarkBlue,
                    style = MaterialTheme.typography.bodyLarge
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFE0E0E0)) // Separador

                // Contenido de la Notificación
                Text(
                    text = "Mensaje:",
                    fontWeight = FontWeight.SemiBold,
                    color = LivelyGreen,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = notification.content,
                    color = LivelyDarkBlue,
                    style = MaterialTheme.typography.bodyMedium
                )

                Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFE0E0E0)) // Separador

                // Estado de la Notificación
                Text(
                    text = "Estado:",
                    fontWeight = FontWeight.SemiBold,
                    color = LivelyGreen,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = notification.status,
                    color = LivelyDarkBlue,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp)) // Más espacio antes de los botones

                // --- Botones de Acción ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botón de MARCAR LEÍDO / DESARCHIVAR
                    if (!isRead) { // Muestra si el estado NO es READ
                        Button(
                            onClick = {
                                viewModel.markNotificationAsRead(notification.id)
                                onActionSuccess()
                            },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            enabled = !viewModel.isLoading,
                            shape = RoundedCornerShape(10.dp), // Esquinas ligeramente más suaves
                            colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen)
                        ) {
                            Text(readButtonText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f).padding(end = 8.dp))
                    }


                    // Botón ARCHIVAR
                    if (!isArchived) { // Solo muestra Archivar si NO está archivado
                        Button(
                            onClick = {
                                viewModel.markNotificationAsArchived(notification.id)
                                onActionSuccess()
                            },
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            enabled = !viewModel.isLoading,
                            shape = RoundedCornerShape(10.dp), // Esquinas ligeramente más suaves
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6A1B9A) // Púrpura
                            )
                        ) {
                            Text(
                                text = "Archivar",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de cierre en la parte inferior
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = LivelyDarkBlue) // Botón de cierre más discreto
                ) {
                    Text("Cerrar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun FloatingCreateAlertButton(
    viewModel: NotificationsViewModel,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showDialog = true },
        containerColor = Color.Red,
        contentColor = Color.White,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.size(65.dp)
    ) {
        Text("+", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    }

    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        var selectedUserId by remember { mutableLongStateOf(0) }
        val familyMembers = viewModel.familyMembers
        val isLoading = viewModel.isLoading

        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Crear Alerta",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = LivelyDarkBlue
                    )

                    // Título
                    androidx.compose.material3.OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Contenido
                    androidx.compose.material3.OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Contenido") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selector de usuario/familiar
                    Text("Seleccionar familiar:", fontWeight = FontWeight.SemiBold)
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (familyMembers.isEmpty()) {
                        Text("No hay familiares disponibles.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.height(150.dp) // Altura limitada para muchos miembros
                        ) {
                            items(familyMembers) { member ->
                                Button(
                                    onClick = { selectedUserId = member.userId.value },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedUserId == member.userId.value) LivelyGreen else Color(0xFFE0E0E0)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text("${member.firstName} ${member.lastName}", color = if (selectedUserId == member.userId.value) Color.White else LivelyDarkBlue)
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                viewModel.sendNotification(title, content, selectedUserId)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen),
                            enabled = title.isNotBlank() && content.isNotBlank()
                        ) {
                            Text("Enviar")
                        }
                    }
                }
            }
        }

        // Carga los miembros de la familia si aún no están
        LaunchedEffect(Unit) {
            viewModel.loadFamilyMembers()
        }
    }
}
