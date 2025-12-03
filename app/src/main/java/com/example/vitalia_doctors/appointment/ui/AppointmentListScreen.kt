package com.example.vitalia_doctors.appointment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vitalia_doctors.model.beans.appointment.Appointment
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.ui.theme.LivelyOffWhite
import com.example.vitalia_doctors.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentListScreen(navController: NavController, viewModel: AppointmentViewModel) {

    val appointmentsState by viewModel.appointmentsState.collectAsState()

    // Supongamos que el ID del doctor se obtiene de las SharedPreferences o similar
    val doctorId = 1L // Reemplazar con la lógica real para obtener el ID del doctor
    viewModel.loadAppointmentsByDoctor(doctorId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citas", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = LivelyGreen
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("appointments_create") },
                containerColor = LivelyGreen
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Cita", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LivelyOffWhite)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Espacio superior reducido

            when (val result = appointmentsState) {
                is Result.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(result.data) { appointment ->
                            AppointmentCard(appointment = appointment, navController = navController)
                        }
                    }
                }
                is Result.Error -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("Error: ${result.exception.message}", color = Color.Red, fontSize = 18.sp)
                    }
                }
                null -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(color = LivelyGreen)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCard(appointment: Appointment, navController: NavController) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { navController.navigate("appointments_detail/${appointment.id}") }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Paciente ID: ${appointment.residentId}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Fecha: ${appointment.date}", fontSize = 16.sp)
            Text("Hora: ${appointment.time}", fontSize = 16.sp)
            Text("Estado: ${appointment.status}", fontSize = 16.sp, color = if (appointment.status == "CONFIRMED") LivelyGreen else Color.Gray)
        }
    }
}
