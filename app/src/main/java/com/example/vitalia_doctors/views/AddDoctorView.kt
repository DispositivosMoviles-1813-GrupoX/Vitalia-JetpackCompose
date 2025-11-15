package com.example.vitalia_doctors.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vitalia_doctors.components.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoctorScreen() {
    // 1. Estados para los campos
    var fullName by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Estructura de la barra superior (Header)
            TopAppBar(
                title = { Text("Add Doctor") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de retroceso */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        bottomBar = {
            // Estructura de los botones inferiores (Cancel / Save)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón "Cancel"
                OutlinedButton(
                    onClick = { /* Acción de cancelar */ },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Cancel")
                }
                // Botón "Save" (en color verde)
                Button(
                    onClick = { /* Acción de guardar */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        // Replicar el color verde
                        containerColor = Color(25, 230, 128)
                    )
                ) {
                    Text("Save")
                }
            }
        }
    ) { paddingValues ->
        // Contenido principal: El formulario
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espacio superior

            // ## Sección: Doctor Information
            Text(
                text = "Doctor Information",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2E7D32) // Color verde oscuro similar al de la imagen
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo 1: Full Name
            CustomTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            // Campo 2: Specialty
            CustomTextField(
                value = specialty,
                onValueChange = { specialty = it },
                label = "Specialty",
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) } // Usando el icono de bolsa médica
            )

            // Campo 3: Phone
            CustomTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
            )

            // Campo 4: Email
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddDoctorScreen() {
    AddDoctorScreen()
}