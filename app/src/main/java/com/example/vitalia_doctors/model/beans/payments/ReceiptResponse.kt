package com.example.vitalia_doctors.model.beans.payments

data class ReceiptResponse(
    val receiptId: Long,
    val issueDate: String,
    val dueDate: String,
    val totalAmount: Double,
    val status: Boolean,
    val residentId: Long,
    val paymentId: Long?,
    val paymentDate: String?,
    val amountPaid: Double,
    val paymentMethod: Int,
    val type: String
)
