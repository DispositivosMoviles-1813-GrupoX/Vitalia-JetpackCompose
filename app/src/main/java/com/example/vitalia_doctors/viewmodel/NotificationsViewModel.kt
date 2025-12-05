package com.example.vitalia_doctors.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.model.beans.notifications.FamilyMemberResponse
import com.example.vitalia_doctors.model.beans.notifications.NotificationRequest
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

    // Resultado de la notificación creada
    var createdNotification: NotificationResponse? by mutableStateOf(null)
        private set

    var familyMembers: List<FamilyMemberResponse> by mutableStateOf(emptyList())
        private set

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

    /**
     * Función para buscar notificaciones por estado ASOCIADAS A UN USUARIO ESPECÍFICO.
     * Esta es la nueva función que usa la combinación @Path y @Query.
     * @param userId El ID del usuario.
     * @param status El estado de la notificación a buscar (READ, UNREAD, etc.).
     */
    fun getNotificationsByUserIdAndStatus(userId: Long, status: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // Ejecución en el hilo de IO
                val response = withContext(Dispatchers.IO) {
                    // Llama al nuevo servicio Retrofit
                    RetrofitClient.webService.getNotificationsByUserIdAndStatus(userId, status)
                }

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualizamos la lista con los resultados filtrados
                    notificationsList = response.body() ?: emptyList()
                } else {
                    // Manejo de errores HTTP
                    errorMessage = "Error al filtrar por estado '$status': Código ${response.code()}"
                }
            } catch (e: Exception) {
                // Manejo de errores de conexión/deserialización
                errorMessage = "Error de red al filtrar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Función para buscar notificaciones por estado (READ, UNREAD, ARCHIVED, UNARCHIVED).
     * @param status El estado de la notificación a buscar.
     */
    fun searchNotificationsByStatus(status: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                // Ejecución en el hilo de IO
                val response = withContext(Dispatchers.IO) {
                    // Llamada al nuevo servicio Retrofit usando @Query
                    RetrofitClient.webService.getNotificationByStatus(status)
                }

                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, actualizamos la lista con los resultados filtrados
                    notificationsList = response.body() ?: emptyList()
                } else {
                    // Manejo de errores HTTP
                    errorMessage = "Error al filtrar por estado '$status': Código ${response.code()}"
                }
            } catch (e: Exception) {
                // Manejo de errores de conexión/deserialización
                errorMessage = "Error de red al filtrar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Marca una notificación específica como LEÍDA.
     * @param notificationId El ID de la notificación a actualizar.
     */
    fun markNotificationAsRead(notificationId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.webService.changeStatusToRead(notificationId)
                }

                if (response.isSuccessful) {
                    // Opcional: Actualiza la lista localmente o recarga los datos desde el servidor
                    updateNotificationStatusLocally(notificationId, "READ")
                } else {
                    errorMessage = "Error al marcar como leído: Código ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de red al marcar como leído: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Marca una notificación específica como ARCHIVADA.
     * @param notificationId El ID de la notificación a actualizar.
     */
    fun markNotificationAsArchived(notificationId: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.webService.changeStatusToArchived(notificationId)
                }

                if (response.isSuccessful) {
                    // Opcional: Actualiza la lista localmente o recarga los datos desde el servidor
                    updateNotificationStatusLocally(notificationId, "ARCHIVED")
                } else {
                    errorMessage = "Error al archivar: Código ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de red al archivar: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun updateNotificationStatusLocally(id: Long, newStatus: String) {
        notificationsList = notificationsList.map { notification ->
            if (notification.id == id) {
                // Crea una copia de la notificación con el nuevo estado
                notification.copy(status = newStatus)
            } else {
                notification
            }
        }
    }

    fun sendNotification(title: String, content: String, userId: Long) {
        val request = NotificationRequest(
            title = title,
            content = content,
            userId = userId
        )

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            createdNotification = null

            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.webService.makeAlertNotification(request)
                }

                if (response.isSuccessful) {
                    createdNotification = response.body()
                } else {
                    errorMessage = "Error al enviar notificación: Código ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "Error de red: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Obtiene todos los familiares registrados
     */
    fun loadFamilyMembers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.webService.getFamilyMembers()
                }

                if (response.isSuccessful) {
                    familyMembers = response.body() ?: emptyList()
                } else {
                    errorMessage = "Error al obtener familiares: Código ${response.code()}"
                }

            } catch (e: Exception) {
                errorMessage = "Error de red: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}