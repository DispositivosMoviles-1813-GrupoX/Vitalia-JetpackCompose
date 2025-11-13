package com.example.vitalia_doctors.model.beans.notifications

data class NotificationRequest (
    val title: String,
    val content: String,
    val userId: Long
)