package com.example.vitalia_doctors.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vitalia_doctors.model.beans.Doctor
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.ui.theme.LivelyDarkBlue
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite

@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()

    // Estados para la carga de datos
    var doctorData by remember { mutableStateOf<Doctor?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Efecto de carga inicial
    LaunchedEffect(Unit) {
        try {
            // 1. Obtenemos el ID guardado en SharedPreferences
            val userId = RetrofitClient.getUserId()

            if (userId != 0L) {
                // 2. Hacemos la llamada usando Corutinas (ya que tu WebService es 'suspend')
                val response = RetrofitClient.webService.getDoctorProfile(userId)

                if (response.isSuccessful) {
                    doctorData = response.body()
                } else {
                    Log.e("Profile", "Error backend: ${response.code()}")
                }
            } else {
                Log.e("Profile", "No se encontró ID de usuario logueado")
            }
        } catch (e: Exception) {
            Log.e("Profile", "Error de red: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // Preparación de datos para la UI
    val doctorName = if (doctorData != null) "Dr. ${doctorData!!.fullName.firstName} ${doctorData!!.fullName.lastName}" else "Cargando..."
    val specialty = doctorData?.specialty ?: "..."
    val license = doctorData?.licenseNumber ?: "..."
    // val email = "..." // El email no viene en el objeto Doctor, sino en User. Puedes dejarlo fijo o pedirlo aparte.
    val phone = doctorData?.contactInfo?.phone ?: "..."
    val address = doctorData?.contactInfo?.address?.street ?: "..."

    // --- UI (DISEÑO) ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LivelyOffWhite)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "Mi Perfil",
            color = LivelyGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(Modifier.height(32.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = LivelyGreen,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(text = doctorName, color = LivelyDarkBlue, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = specialty, color = Color.Gray, fontSize = 18.sp)

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Información Personal",
            color = LivelyDarkBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(color = LivelyGreen, modifier = Modifier.padding(16.dp))
        } else {
            ProfileInfoCard(icon = Icons.Default.Star, label = "Licencia", value = license)
            ProfileInfoCard(icon = Icons.Default.Phone, label = "Teléfono", value = phone)
            ProfileInfoCard(icon = Icons.Default.LocationOn, label = "Dirección", value = address)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                // Lógica simple de Logout
                RetrofitClient.clearSession()
                // Aquí deberías navegar al Login
            },
            modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(text = "Cerrar Sesión", fontSize = 18.sp, color = Color.White)
        }
    }
}

// Componente reutilizable para las tarjetas
@Composable
fun ProfileInfoCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = LivelyGreen, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontSize = 12.sp, color = Color.Gray)
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = LivelyDarkBlue)
            }
        }
    }
}