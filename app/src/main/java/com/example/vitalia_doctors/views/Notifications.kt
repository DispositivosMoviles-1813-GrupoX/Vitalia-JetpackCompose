package com.example.vitalia_doctors.views

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
    // 2. Obtener el estado del ViewModel
    val notifications = viewModel.notificationsList
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // 3. Estado del filtro seleccionado
    var selectedFilter by remember { mutableStateOf("ALL") }

    // 4. Obtener el userId de SharedPreferences solo una vez
    val currentUserId by remember {
        val pref = mainActivity.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        mutableLongStateOf(pref.getLong("userId", 0L))
    }

    // 5. Función para manejar la lógica de carga/filtrado
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

    // 6. Efecto para cargar las notificaciones al inicio o al cambiar el filtro
    LaunchedEffect(selectedFilter) {
        loadNotifications(selectedFilter)
    }

    // 7. Contenedor principal
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        FilterButtonsRow(
            selectedFilter = selectedFilter,
            onFilterSelected = { filter -> selectedFilter = filter }
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                // Muestra un indicador de progreso mientras carga
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = LivelyGreen
                )
            }
            errorMessage != null -> {
                // Muestra el mensaje de error
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            notifications.isEmpty() -> {
                // Muestra mensaje si no hay notificaciones
                Text(
                    text = "No hay notificaciones para mostrar.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                // 6. Lista de Notificaciones
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationCard(notification = notification)
                    }
                }
            }
        }
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
                    containerColor = if (isSelected) LivelyGreen else Color.LightGray,
                    contentColor = if (isSelected) Color.White else LivelyDarkBlue
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(filter.capitalize(), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

/**
 * Componente Composable para mostrar una sola notificación en una tarjeta simple.
 */
@Composable
fun NotificationCard(notification: NotificationResponse) {
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
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // Color de fondo basado en el estado
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                fontWeight = if (notification.status == "UNREAD") FontWeight.Bold else FontWeight.Normal,
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

            // 4. Botón para mostrar el detalle
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 10.dp),
            ) {
                Text("Ver Detalle", style = MaterialTheme.typography.labelSmall)
            }
        }
    }

    // Muestra el diálogo si showDialog es true
    if (showDialog) {
        NotificationDetailDialog(
            notification = notification,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * Componente Diálogo para mostrar la información completa de la notificación.
 */
@Composable
fun NotificationDetailDialog(notification: NotificationResponse, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Encabezado con título y botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle de Notificación",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
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

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de cierre en la parte inferior
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Entendido")
                }
            }
        }
    }
}