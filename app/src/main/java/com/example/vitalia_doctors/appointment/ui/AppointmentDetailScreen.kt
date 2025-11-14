package com.example.vitalia_doctors.appointment.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailScreen(navController: NavController, viewModel: AppointmentViewModel, appointmentId: Long?) {

    appointmentId?.let { viewModel.getAppointmentDetail(it) }

    val appointmentState by viewModel.appointmentDetailState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cita", fontWeight = FontWeight.Bold) },
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
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val result = appointmentState) {
                is Result.Success -> {
                    val appointment = result.data
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Paciente ID: ${appointment.residentId}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Fecha: ${appointment.date}", fontSize = 20.sp)
                        Text("Hora: ${appointment.time}", fontSize = 20.sp)
                        Text("Estado: ${appointment.status}", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Motivo:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(appointment.reason ?: "No especificado", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Notas:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(appointment.notes ?: "Sin notas", fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = { /* Lógica de Actualizar */ }, colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen)) {
                                Text("Actualizar")
                            }
                            Button(onClick = { 
                                viewModel.deleteAppointment(appointment.appointmentId)
                                navController.popBackStack()
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                                Text("Eliminar")
                            }
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
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}