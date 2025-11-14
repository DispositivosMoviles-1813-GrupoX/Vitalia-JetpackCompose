package com.example.vitalia_doctors.views

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.model.beans.notifications.NotificationResponse
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.viewmodel.NotificationsViewModel

@Composable
fun Notifications(viewModel: NotificationsViewModel = viewModel(), mainActivity: MainActivity) {
    // 2. Obtener el estado del ViewModel
    val notifications = viewModel.notificationsList
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    // 3. Efecto para cargar las notificaciones al inicio
    LaunchedEffect(key1 = Unit) {
        val pref = mainActivity.getSharedPreferences("pref1", Context.MODE_PRIVATE)
        val currentUserId = pref.getLong("userId", 0L)
        // Solo cargar si el ID es válido (mayor a 0)
        if (currentUserId > 0L) {
            viewModel.getNotificationsByUserId(currentUserId)
        } else {
            // Establecer un mensaje de error si no hay sesión iniciada
            viewModel.errorMessage = "No se encontró el ID de usuario. Inicie sesión nuevamente."
        }
    }

    // 4. Contenedor principal
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

        // 5. Mostrar diferentes estados de carga/error
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

/**
 * Componente Composable para mostrar una sola notificación en una tarjeta simple.
 */
@Composable
fun NotificationCard(notification: NotificationResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            // Cambia el color si es una notificación "importante" o no leída
            containerColor = if (notification.status == "UNREAD") Color(0xFFFFFBE0) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de advertencia o relevante
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Notification Icon",
                tint = LivelyGreen,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Título
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    color = LivelyDarkBlue,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Contenido/Mensaje
                Text(
                    text = notification.content,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Opcional: Mostrar el estado o timestamp
            Text(
                text = notification.status,
                color = if (notification.status == "UNREAD") LivelyGreen else Color.LightGray,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.Top)
            )
        }
    }
}