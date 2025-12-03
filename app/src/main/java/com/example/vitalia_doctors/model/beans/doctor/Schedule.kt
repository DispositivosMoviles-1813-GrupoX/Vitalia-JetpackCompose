package com.example.vitalia_doctors.model.beans.doctor

import com.google.gson.annotations.SerializedName

data class Schedule(
    val id: Long,
    val day: String,
    @SerializedName("startTime") // El nombre en el JSON puede ser diferente
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    val appointmentId: Long?
)
