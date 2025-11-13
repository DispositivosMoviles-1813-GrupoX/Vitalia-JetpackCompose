package com.example.vitalia_doctors.views

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.vitalia_doctors.MainActivity
import com.example.vitalia_doctors.model.beans.iam.LogInRequest
import com.example.vitalia_doctors.model.client.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun LogIn(recordarPantalla: NavHostController, mainActivity: MainActivity) {
    val pref: SharedPreferences? =
        mainActivity.getSharedPreferences("pref1", Context.MODE_PRIVATE)

    val check:Boolean= pref!!.getBoolean("check",false)
    val username:String=pref.getString("usu","")!!
    val password:String=pref.getString("pas","")!!
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }


    var txtUsu by remember { mutableStateOf(username) }
    var txtPass by remember { mutableStateOf(password) }
    var chk by remember { mutableStateOf(check) }
    var isDisplay by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Login",
            fontSize = 32.sp,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = txtUsu,
            modifier = Modifier.padding(7.dp)
                .padding(vertical = 15.dp),
            label = { Text(text = "Insert Username") },
            placeholder = { Text("Insert Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Blue,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(240, 240,
                    240),
            ),
            onValueChange = {
                txtUsu = it
            }
        )

        OutlinedTextField(
            value = txtPass,
            modifier = Modifier.padding(7.dp)
                .padding(vertical = 15.dp),
            label = { Text(text = "Insert password") },
            placeholder = { Text("Insert password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    tint = Color.Gray,
                    contentDescription = ""
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Blue,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color(240, 240,
                    240),
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                txtPass = it
            }
        )

        if(isDisplay){
            Dialog(
                onDismissRequest = {isDisplay=false}
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,

                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Text(text = "ERROR",
                            fontSize = 25.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center)

                        Spacer(modifier= Modifier.size(20.dp))

                        Text(
                            text = message,
                            fontSize = 18.sp,
                            color = Color.Blue,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier= Modifier.size(20.dp))

                        Button(
                            onClick = {
                                isDisplay=false
                            }
                        ) {
                            Text(text="Volver a Intentar",
                                fontSize = 18.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center)
                        }
                    }

                }
            }
        }

        ElevatedButton(
            colors =
                ButtonDefaults.buttonColors(
                    Color(50, 50, 250,
                        250)
                ),
            modifier = Modifier
                .padding(10.dp)
                .padding(vertical = 10.dp)
                .width(300.dp),
            onClick = {
                scope.launch {
                    try {
                        val response = RetrofitClient.webService.signIn(
                            LogInRequest(
                                username = txtUsu,
                                password = txtPass
                            )
                        )

                        if (response.isSuccessful) {
                            val body = response.body()

                            // Guarda token o datos si el login fue correcto
                            val editor: SharedPreferences.Editor = pref!!.edit()
                            if (chk) {
                                editor.putString("usu", txtUsu)
                                editor.putString("pas", txtPass)
                                editor.putBoolean("check", true)
                            } else {
                                editor.putString("usu", "")
                                editor.putString("pas", "")
                                editor.putBoolean("check", false)
                            }

                            // Guarda el token si viene en la respuesta
                            body?.token?.let { editor.putString("token", it) }
                            body?.id?.let { editor.putLong("userId", it) }

                            editor.apply()
                            editor.commit()

                            // Navegar al Home
                            recordarPantalla.navigate("Home")

                        } else {
                            // Mostrar mensaje de error del servidor
                            isDisplay = true
                            message = "Credenciales incorrectas o error ${response.code()}"
                        }

                    } catch (e: Exception) {
                        isDisplay = true
                        message = "Error de red: ${e.message}"
                    }
                }
            }
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Row (
            verticalAlignment = Alignment.CenterVertically,
        ){
            Switch(
                checked = chk,
                onCheckedChange = {
                    chk = it
                },
                //NO OBLIGATORIO
                thumbContent = if (chk) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                                .background(Color.Red),
                        )
                    }
                }
            )
            Text(
                text = "Recordar Credenciales",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿No tienes una cuenta?",
                fontSize = 15.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material3.TextButton(
                onClick = {
                    // Navegar a la pantalla de SignUp
                    recordarPantalla.navigate("SignUp")
                }
            ) {
                Text(
                    text = "Regístrate",
                    fontSize = 15.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}