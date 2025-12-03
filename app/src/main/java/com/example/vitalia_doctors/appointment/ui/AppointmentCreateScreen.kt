package com.example.vitalia_doctors.appointment.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vitalia_doctors.model.beans.appointment.Appointment
import com.example.vitalia_doctors.ui.theme.LivelyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCreateScreen(navController: NavController, viewModel: AppointmentViewModel) {

    var residentId by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    // Supongamos que el ID del doctor se obtiene de las SharedPreferences o similar
    val doctorId = 1L // Reemplazar con la lógica real para obtener el ID del doctor

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás", tint = LivelyGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = LivelyGreen
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = residentId, onValueChange = { residentId = it }, label = { Text("ID del Residente") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Fecha (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Hora (HH:MM)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                val newAppointment = Appointment(
                    id = 0L, // Se envía 0, que se convierte a null en el DTO
                    doctorId = doctorId,
                    residentId = residentId.toLongOrNull() ?: 0L,
                    date = date,
                    time = time,
                    status = "SCHEDULED" // Estado por defecto
                )
                viewModel.createAppointment(newAppointment)
                navController.popBackStack()
            }, colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen), modifier = Modifier.fillMaxWidth()) {
                Text("Guardar Cita", fontSize = 18.sp)
            }
        }
    }
}