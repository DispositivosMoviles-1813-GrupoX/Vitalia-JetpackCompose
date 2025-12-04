package com.example.vitalia_doctors.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vitalia_doctors.model.beans.iam.SignUpRequest
import com.example.vitalia_doctors.model.client.RetrofitClient
import com.example.vitalia_doctors.ui.theme.LivelyGreen
import kotlinx.coroutines.launch

@Composable
fun SignUp(recordarPantalla: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = LivelyGreen)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val response = RetrofitClient.webService.signUp(
                            SignUpRequest(
                                username = username,
                                password = password,
                                roles = setOf("ROLE_DOCTOR"), // Asignar rol de doctor
                                emailAddress = email
                            )
                        )
                        if (response.isSuccessful) {
                            message = "Registro exitoso ✅"
                            // Vuelve al login después de un breve delay
                            kotlinx.coroutines.delay(1500)
                            recordarPantalla.navigate("LogIn")
                        } else {
                            message = "Error en el registro: ${response.code()}"
                        }
                    } catch (e: Exception) {
                        message = "Error de red: ${e.message}"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = LivelyGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = if (message.startsWith("Error")) Color.Red else LivelyGreen)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = { recordarPantalla.navigate("LogIn") }
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = LivelyGreen)
        }
    }
}