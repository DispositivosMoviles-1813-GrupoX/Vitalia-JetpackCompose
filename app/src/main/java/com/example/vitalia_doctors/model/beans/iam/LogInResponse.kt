package com.example.vitalia_doctors.model.beans.iam

data class LogInResponse (
    val id: Long,
    val username: String,
    val token: String
)