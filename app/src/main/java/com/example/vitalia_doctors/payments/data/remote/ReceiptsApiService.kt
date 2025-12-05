package com.example.vitalia_doctors.payments.data.remote

import com.example.vitalia_doctors.model.beans.payments.ReceiptResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceiptsApiService {

    @GET("receipts")
    suspend fun getReceipts(): Response<List<ReceiptResponse>>

    // GET /api/v1/receipts/{residentId}
    @GET("receipts/{residentId}")
    suspend fun getReceiptsByResident(
        @Path("residentId") residentId: Long
    ): Response<List<ReceiptResponse>>

    // GET /api/v1/receipts/searchByReceiptId?receiptId=...
    @GET("receipts/searchByReceiptId")
    suspend fun getReceiptById(
        @Query("receiptId") receiptId: Long
    ): Response<ReceiptResponse>

    @GET("receipts/payment/{paymentId}")
    suspend fun getReceiptsByPaymentId(
        @Path("paymentId") paymentId: Long
    ): Response<List<ReceiptResponse>>

}
