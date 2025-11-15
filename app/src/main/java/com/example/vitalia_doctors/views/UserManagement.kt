package com.example.vitalia_doctors.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource // Necesario para cargar imágenes
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vitalia_doctors.R

// --- Modelos de Datos ---
data class User(
    val id: Int,
    val name: String,
    val role: String,
    // En una aplicación real, aquí pondrías el ID de la imagen (drawable resource)
    val imageId: Int
)

// Datos de ejemplo para la lista
val sampleUsers = listOf(
    User(1, "Dr. Emily Carter", "Doctor", R.drawable.missing),
    User(2, "Sarah Johnson", "Nurse", R.drawable.missing),
    User(3, "Mark Thompson", "Family", R.drawable.missing),
    User(4, "David Lee", "Admin", R.drawable.missing)
)

// Colores personalizados (asumiendo que mantienes la paleta anterior)
val TopBarColor = Color(0xFF90EE90)      // Verde claro para la TopBar
val ButtonGreen = Color(0xFF66BB6A)      // Verde para los botones Create/View
val DeleteButtonRed = Color(241, 225, 224)  // Rojo pastel para el botón Delete
val DeleteButtonTextRed = Color(239,59,59) // Rojo oscuro para el texto del botón Delete

// -----------------------------------------------------------------
// ## Pantalla Principal: User Management
// -----------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    users: List<User> = sampleUsers,
    onNavigateBack: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopBarColor // Color de la Top Bar
                )
            )
        },
        bottomBar = {
            ActionButtonsBar(
                onCreateClick = onCreateClick,
                onViewClick = onViewClick,
                onDeleteClick = onDeleteClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F7)) // Un fondo muy claro para contrastar con la lista
        ) {

            // Contenedor principal de la lista (simulando una Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Users",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Lista de Usuarios
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        items(users) { user ->
                            UserListItem(user = user)
                            // Divider para separar los ítems, excepto el último
                            if (user != users.last()) {
                                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp, modifier = Modifier.padding(start = 72.dp, end = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------
// ## Componente: Ítem de Usuario en la Lista
// -----------------------------------------------------------------

@Composable
fun UserListItem(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen de Perfil
        Image(
            painter = painterResource(id = user.imageId),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray) // Fondo por si la imagen no carga
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre y Rol
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = user.role,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray // Color gris para el rol
            )
        }

        // Icono de Opciones (tres puntos verticales)
        IconButton(onClick = { /* Acción de menú de opciones */ }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "Options")
        }
    }
}

// -----------------------------------------------------------------
// ## Componente: Barra de Botones de Acción Inferior
// -----------------------------------------------------------------

@Composable
fun ActionButtonsBar(
    onCreateClick: () -> Unit,
    onViewClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Fila 1: Botones Create y View
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón Create (Verde primario)
            Button(
                onClick = onCreateClick,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Create")
            }

            // Botón View (Verde claro con borde)
            OutlinedButton(
                onClick = onViewClick,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ButtonGreen)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("View")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila 2: Botón Delete User
        Button(
            onClick = onDeleteClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DeleteButtonRed, // Rojo pastel
                contentColor = DeleteButtonTextRed // Rojo oscuro para el texto/icono
            )
        ) {
            Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Delete User")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserManagementScreen() {
    UserManagementScreen()
}