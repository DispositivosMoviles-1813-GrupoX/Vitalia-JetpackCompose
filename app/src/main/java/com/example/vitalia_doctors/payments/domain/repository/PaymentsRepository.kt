package com.example.vitalia_doctors.payments.domain.repository

import com.example.vitalia_doctors.model.beans.payments.ReceiptResponse
import com.example.vitalia_doctors.utils.Result

interface PaymentsRepository {
    suspend fun getReceipts(): Result<List<ReceiptResponse>>
    suspend fun getReceiptsByResident(residentId: Long): Result<List<ReceiptResponse>>
    suspend fun getPaymentsForDoctor(paymentId: Long): Result<List<ReceiptResponse>>
    suspend fun getReceiptById(receiptId: Long): Result<ReceiptResponse?>


}
