package com.example.vitalia_doctors.payments.domain.repository

import android.util.Log
import com.example.vitalia_doctors.model.beans.payments.ReceiptResponse
import com.example.vitalia_doctors.payments.data.remote.ReceiptsApiService
import com.example.vitalia_doctors.utils.Result

class PaymentsRepositoryImpl(
    private val apiService: ReceiptsApiService
) : PaymentsRepository {

    override suspend fun getReceipts(): Result<List<ReceiptResponse>> {

        return try {
            val response = apiService.getReceipts()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Log.e("PaymentsRepository", "Error cargando recibos", e)
            return Result.Error(e)
        }

    }

    override suspend fun getReceiptsByResident(residentId: Long): Result<List<ReceiptResponse>> {
        return try {
            val response = apiService.getReceiptsByResident(residentId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Log.e("PaymentsRepository", "Error cargando recibos", e)
            return Result.Error(e)
        }

    }

    override suspend fun getPaymentsForDoctor(paymentId: Long): Result<List<ReceiptResponse>> {
        return try {
            val response = apiService.getReceiptsByPaymentId(paymentId)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Log.e("PaymentsRepository", "Error cargando pagos por paymentId", e)
            Result.Error(e)
        }
    }
    override suspend fun getReceiptById(receiptId: Long): Result<ReceiptResponse?> {
        return try {
            val response = apiService.getReceiptById(receiptId)
            if (response.isSuccessful) {
                Result.Success(response.body())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Log.e("PaymentsRepository", "Error cargando recibos", e)
            return Result.Error(e)
        }

    }


}
