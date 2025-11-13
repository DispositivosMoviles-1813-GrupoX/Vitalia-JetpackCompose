package com.example.vitalia_doctors.model.beans.notifications

data class NotificationResponse (
    val id: Long,
    val title: String,
    val content: String,
    val status: String,
    val userId: Long
)