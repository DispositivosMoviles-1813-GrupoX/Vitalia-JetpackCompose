package com.example.vitalia_doctors.model.beans.iam

// No se necesita ninguna anotación especial si el nombre del campo en el JSON
// coincide con el nombre de la variable (en este caso, "token").
data class LogInResponse (
    val id: Long,
    val username: String,
    val token: String
)
