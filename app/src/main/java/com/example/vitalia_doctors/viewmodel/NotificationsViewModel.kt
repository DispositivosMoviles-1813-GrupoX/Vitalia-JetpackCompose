package com.example.vitalia_doctors.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.model.beans.notifications.NotificationResponse
import com.example.vitalia_doctors.model.client.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsViewModel() : ViewModel() {
    var notificationsList: List<NotificationResponse> by mutableStateOf(emptyList())
        private set

    // Estado para gestionar si la carga está en curso
    var isLoading: Boolean by mutableStateOf(false)
        private set

    // Estado para manejar mensajes de error
    var errorMessage: String? by mutableStateOf(null)

    /**
     * Función para obtener todas las notificaciones de un usuario.
     * @param userId El ID del usuario cuyas notificaciones se van a buscar.
     */
    fun getNotificationsByUserId(userId: Long) {
        // Ejecutamos la coroutine en el ámbito del ViewModel
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // Ejecución en el hilo de IO
                val response = withContext(Dispatchers.IO) {
                    // Llamada directa al servicio Retrofit
                    RetrofitClient.webService.getNotifications(userId)
                }

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualizamos la lista
                    notificationsList = response.body() ?: emptyList()
                } else {
                    // Manejo de errores HTTP
                    errorMessage = "Error al cargar notificaciones: Código ${response.code()}"
                }
            } catch (e: Exception) {
                // Manejo de errores de conexión/deserialización
                errorMessage = "Error de red: ${e.message}"
            } finally {
                // Finalizamos el estado de carga
                isLoading = false
            }
        }
    }

}