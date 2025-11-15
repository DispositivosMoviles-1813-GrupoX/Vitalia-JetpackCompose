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
import com.example.vitalia_doctors.components.CustomDropdownField

// Opciones de ejemplo para los campos de selección (Dropdown)
val shiftOptions = listOf("Day (7am-3pm)", "Evening (3pm-11pm)", "Night (11pm-7am)")
val departmentOptions = listOf("Emergency", "ICU", "Pediatrics", "Cardiology", "Oncology")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNurseScreen() {
    // 1. Estados para los campos de texto
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // 2. Estados para los campos de selección
    var selectedShift by remember { mutableStateOf(shiftOptions.first()) }
    var isShiftDropdownExpanded by remember { mutableStateOf(false) }

    var selectedDepartment by remember { mutableStateOf(departmentOptions.first()) }
    var isDeptDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Estructura de la barra superior (Header)
            TopAppBar(
                title = { Text("New Nurse") },
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

            // ---------------------------------------------
            // ## Sección 1: Personal Information
            // ---------------------------------------------
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2E7D32) // Color verde oscuro similar al de la imagen
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo 1: Enter full name (Texto)
            CustomTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Enter full name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            // Campo 2: Select shift (Dropdown)
            CustomDropdownField(
                selectedValue = selectedShift,
                onValueSelected = { selectedShift = it },
                label = "Select shift",
                options = shiftOptions,
                expanded = isShiftDropdownExpanded,
                onExpandedChange = { isShiftDropdownExpanded = it },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
            )

            // Campo 3: Select department (Dropdown)
            CustomDropdownField(
                selectedValue = selectedDepartment,
                onValueSelected = { selectedDepartment = it },
                label = "Select department",
                options = departmentOptions,
                expanded = isDeptDropdownExpanded,
                onExpandedChange = { isDeptDropdownExpanded = it },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ---------------------------------------------
            // ## Sección 2: Contact Information
            // ---------------------------------------------
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2E7D32) // Color verde oscuro
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo 4: Enter phone number (Texto)
            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Enter phone number",
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) }
            )

            // Campo 5: Enter email (Texto)
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Enter email",
                leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CustomDropdownField(
    selectedValue: String,
    onValueSelected: () -> Unit,
    label: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    leadingIcon: @Composable () -> Unit
) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun PreviewNewNurseScreen() {
    NewNurseScreen()
}