package com.example.vitalia_doctors.doctor.ui

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vitalia_doctors.doctor.data.dto.AddressDto
import com.example.vitalia_doctors.doctor.data.dto.ContactInfoDto
import com.example.vitalia_doctors.doctor.data.dto.DoctorDto
import com.example.vitalia_doctors.doctor.data.dto.FullNameDto
import com.example.vitalia_doctors.doctor.data.dto.UpdateDoctorDto
import com.example.vitalia_doctors.model.beans.doctor.Doctor
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import com.example.vitalia_doctors.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(navController: NavController, viewModel: DoctorViewModel) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("pref1", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getLong("userId", -1L)
    val firstName = sharedPreferences.getString("firstName", null)

    val doctorProfileState by viewModel.doctorState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = firstName) {
        if (firstName != null) {
            viewModel.findDoctorProfile(firstName)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Doctor", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = LivelyGreen
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            doctorProfileState?.let { result ->
                val doctor = (result as? Result.Success)?.data
                if (isEditing) {
                    ProfileForm(
                        doctor = doctor,
                        userId = userId,
                        viewModel = viewModel,
                        onSave = { isEditing = false },
                        onCancel = { isEditing = false }
                    )
                } else {
                    ShowProfile(doctor = doctor) { isEditing = true }
                }
            } ?: run {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

// --- FORMULARIO UNIFICADO PARA CREAR Y EDITAR ---
@Composable
fun ProfileForm(doctor: Doctor?, userId: Long, viewModel: DoctorViewModel, onSave: () -> Unit, onCancel: () -> Unit) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    var licenseNumber by remember { mutableStateOf(doctor?.licenseNumber ?: "") }
    var specialty by remember { mutableStateOf(doctor?.specialty ?: "") }
    var docFirstName by remember { mutableStateOf(doctor?.fullName?.firstName ?: "") }
    var docLastName by remember { mutableStateOf(doctor?.fullName?.lastName ?: "") }
    var phone by remember { mutableStateOf(doctor?.contactInfo?.phone ?: "") }
    var street by remember { mutableStateOf(doctor?.contactInfo?.address?.street ?: "") }
    var city by remember { mutableStateOf(doctor?.contactInfo?.address?.city ?: "") }
    var state by remember { mutableStateOf(doctor?.contactInfo?.address?.state ?: "") }
    var zipCode by remember { mutableStateOf(doctor?.contactInfo?.address?.zipCode ?: "") }
    var country by remember { mutableStateOf(doctor?.contactInfo?.address?.country ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileHeader(imageUri = imageUri, fullName = "$docFirstName $docLastName", isEditing = true) {
                imagePickerLauncher.launch("image/*")
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text("Información Profesional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(value = licenseNumber, onValueChange = { licenseNumber = it }, label = { Text("Número de Licencia") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = specialty, onValueChange = { specialty = it }, label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Información Personal y Contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedTextField(value = docFirstName, onValueChange = { docFirstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = docLastName, onValueChange = { docLastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = zipCode, onValueChange = { zipCode = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botones fijos en la parte inferior
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen)) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    // Aquí iría la lógica para subir la imagen (imageUri) y obtener la URL
                    // Por ahora, guardamos los datos de texto.
                    if (doctor == null) {
                        val dto = DoctorDto(licenseNumber, specialty, FullNameDto(docFirstName, docLastName), ContactInfoDto(phone, AddressDto(street, city, state, zipCode, country)), userId)
                        viewModel.createDoctor(dto)
                    } else {
                        val dto = UpdateDoctorDto(licenseNumber, specialty, FullNameDto(docFirstName, docLastName), ContactInfoDto(phone, AddressDto(street, city, state, zipCode, country)))
                        viewModel.updateDoctor(doctor.id, dto)
                    }
                    onSave()
                },
                colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen)
            ) {
                Text("Guardar")
            }
        }
    }
}

// --- VISTA DE SÓLO LECTURA ---
@Composable
fun ShowProfile(doctor: Doctor?, onEditClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ProfileHeader(imageUri = null, fullName = doctor?.fullName?.let { "${it.firstName} ${it.lastName}" } ?: "Sin Perfil", isEditing = false) {}
        Spacer(modifier = Modifier.height(24.dp))

        if (doctor != null) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)){
                Column(modifier = Modifier.padding(16.dp)){
                    Text("Información Profesional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Licencia", value = doctor.licenseNumber)
                    InfoRow(label = "Especialidad", value = doctor.specialty)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)){
                Column(modifier = Modifier.padding(16.dp)){
                    Text("Información de Contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(label = "Teléfono", value = doctor.contactInfo.phone)
                    InfoRow(label = "Dirección", value = "${doctor.contactInfo.address.street}, ${doctor.contactInfo.address.city}")
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center){
                 Text(
                    text = "Por favor, completa tu información de perfil para continuar.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onEditClick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen)) {
            Text(if (doctor != null) "Editar Información" else "Crear Perfil")
        }
    }
}

// --- COMPONENTES REUTILIZABLES ---
@Composable
fun ProfileHeader(imageUri: Uri?, fullName: String, isEditing: Boolean, onImageClick: () -> Unit) {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(2.dp, LivelyGreen, CircleShape)
                .then(
                    if (isEditing) Modifier.clickable { onImageClick() } else Modifier
                )
        ) {
            val bitmap = remember { mutableStateOf<android.graphics.Bitmap?>(null) }
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
            }

            if (bitmap.value != null) {
                Image(
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = "Placeholder", modifier = Modifier.fillMaxSize().padding(24.dp), tint = Color.Gray)
            }

            if (isEditing) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)), contentAlignment = Alignment.Center){
                    Text("CAMBIAR", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}