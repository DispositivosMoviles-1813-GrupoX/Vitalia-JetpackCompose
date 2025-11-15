package com.example.vitalia_doctors.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource // Necesitarás un recurso de imagen
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vitalia_doctors.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen() {
    // 1. Estados para los campos de contacto (simulando que son editables)
    var phoneNumber by remember { mutableStateOf("(123) 456-7890") }
    var emailAddress by remember { mutableStateOf("e.harper@medcenter.com") }

    // Colores personalizados para la cabecera del perfil (Verde muy claro)
    val ProfileHeaderColor = Color(0xFFE8F5E9) // Verde pastel claro
    val PrimaryCareTagColor = Color(0xFFC8E6C9) // Verde ligeramente más oscuro para el tag

    Scaffold(
        topBar = {
            // Estructura de la barra superior (Header)
            TopAppBar(
                title = { Text("Doctor Profile") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de retroceso */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White // Fondo blanco
                )
            )
        },
        bottomBar = {
            // Recreación de la barra de navegación inferior
            BottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ---------------------------------------------
            // ## Sección Superior: Perfil del Doctor
            // ---------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ProfileHeaderColor) // Fondo verde claro
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen de Perfil Circular
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.missing),
                        contentDescription = "Doctor's Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    // Icono de edición (lápiz) sobre la imagen
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit Profile Picture",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(4.dp)
                            .size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre del Doctor
                Text(
                    text = "Dr. Eleanor Harper",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )

                // Especialidad
                Text(
                    text = "Specialist in Geriatric Medicine",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tag "Primary Care"
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PrimaryCareTagColor
                    )
                ) {
                    Text(
                        text = "Primary Care",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2E7D32), // Color de texto verde oscuro
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // ---------------------------------------------
            // ## Sección Central: Contact Information
            // ---------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Contact Information",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                // Campo 1: Phone Number
                CustomTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone Number",
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) }
                )

                // Campo 2: Email Address
                CustomTextField(
                    value = emailAddress,
                    onValueChange = { emailAddress = it },
                    label = "Email Address",
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null, tint = Color.Gray) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón "Save Changes" (Verde)
                Button(
                    onClick = { /* Acción de guardar */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF66BB6A) // El verde del botón Guardar
                    ),
                    shape = RoundedCornerShape(12.dp) // Forma redondeada
                ) {
                    Text("Save Changes", fontSize = 16.sp)
                }
            }
        }
    }
}

// ---------------------------------------------
// ## Componente de Barra de Navegación Inferior
// ---------------------------------------------

@Composable
fun BottomNavigationBar() {
    val items = listOf("Home", "Residents", "Messages", "Reports", "Settings")
    val icons = listOf(
        Icons.Filled.Home, Icons.Filled.Face, Icons.Filled.Email,
        Icons.Filled.Person, Icons.Filled.Settings
    )
    val selectedItem = remember { mutableStateOf(4) } // Settings está seleccionado en la imagen

    NavigationBar(
        containerColor = Color.White // Fondo blanco
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem.value == index,
                onClick = { selectedItem.value = index /* Acción de navegación */ },
                colors = NavigationBarItemDefaults.colors(
                    // Color de los iconos seleccionados y no seleccionados
                    selectedIconColor = Color(0xFF66BB6A),
                    selectedTextColor = Color(0xFF66BB6A),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White // Para que no muestre color de fondo al seleccionar
                )
            )
        }
    }
}

// Dummy Composable para la función que necesita ser definida en otro archivo
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit
) {
    // Implementación mínima para que el Preview compile
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0FFF0),
            unfocusedContainerColor = Color(0xFFF0FFF0),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDoctorProfileScreen() {
    DoctorProfileScreen()
}