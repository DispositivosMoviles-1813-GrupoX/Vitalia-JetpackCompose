package com.example.vitalia_doctors.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vitalia_doctors.components.CustomTextField

// Definición de las opciones de ejemplo para el Dropdown
val caregiverOptions = listOf("Alice Johnson", "Bob Smith", "Charlie Brown", "Diana Prince")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddResidentScreen() {
    // 1. Estados para los campos de texto
    var residentName by remember { mutableStateOf("") }
    var residentAge by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }

    // 2. Estados para el campo de selección (Dropdown)
    var expanded by remember { mutableStateOf(false) }
    var selectedCaregiver by remember { mutableStateOf(caregiverOptions.first()) }

    Scaffold(
        topBar = {
            // Estructura de la barra superior (Header)
            TopAppBar(
                title = { Text("Add Resident") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de retroceso */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    // Puedes ajustar los colores para que coincidan con la imagen original
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
                // Botón "Save" (en color verde como en la imagen)
                Button(
                    onClick = { /* Acción de guardar */ },
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        // Esto intenta replicar el color verde de "Save"
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
                .verticalScroll(rememberScrollState()), // Permite el desplazamiento si es necesario
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp)) // Espacio superior

            // --- Campo 1: Resident's Name ---
            Text(text = "Resident's Name", style = MaterialTheme.typography.labelLarge)
            CustomTextField(
                value = residentName,
                onValueChange = { residentName = it },
                label = "Enter full name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )

            // --- Campo 2: Resident's Age ---
            Text(text = "Resident's Age", style = MaterialTheme.typography.labelLarge)
            CustomTextField(
                value = residentAge,
                onValueChange = { residentAge = it },
                label = "Enter age",
                leadingIcon = { Icon(Icons.Default.Face, contentDescription = null) } // Usando un icono similar
            )

            // --- Campo 3: Diagnosis ---
            Text(text = "Diagnosis", style = MaterialTheme.typography.labelLarge)
            CustomTextField(
                value = diagnosis,
                onValueChange = { diagnosis = it },
                label = "Primary diagnosis",
                leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null) } // Usando un icono similar
            )

            // --- Campo 4: Assigned Caregiver (Dropdown/Exposed Menu) ---
            Text(text = "Assigned Caregiver", style = MaterialTheme.typography.labelLarge)

            //
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedCaregiver,
                    onValueChange = { }, // No se permite la edición directa
                    label = { Text("Select a caregiver") },
                    leadingIcon = { Icon(Icons.Default.List, contentDescription = null) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall, // Forma del campo
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors() // Colores del campo
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    caregiverOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedCaregiver = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAddResidentScreen() {
    // Para la vista previa
    AddResidentScreen()
}