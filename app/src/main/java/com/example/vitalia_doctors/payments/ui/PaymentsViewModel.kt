package com.example.vitalia_doctors.payments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vitalia_doctors.model.beans.payments.ReceiptResponse
import com.example.vitalia_doctors.payments.domain.repository.PaymentsRepository
import com.example.vitalia_doctors.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

data class PaymentHistoryItem(
    val receiptId: Long,
    val residentId: Long,
    val residentLabel: String,
    val statusLabel: String,
    val issueDateLabel: String,
    val dueDateLabel: String,
    val totalAmountLabel: String,
    val amountPaidLabel: String,
    val paymentMethodLabel: String
)

data class PaymentsSummary(
    val totalToCollectLabel: String = "—",
    val totalCollectedLabel: String = "—",
    val pendingCount: Int = 0
)

data class DoctorPaymentsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val receipts: List<PaymentHistoryItem> = emptyList(),
    val summary: PaymentsSummary = PaymentsSummary(),
    val selectedResidentId: Long? = null
) {
    val filteredReceipts: List<PaymentHistoryItem>
        get() = selectedResidentId?.let { id ->
            receipts.filter { it.residentId == id }
        } ?: receipts

    val residents: List<Long>
        get() = receipts.map { it.residentId }.distinct()
}

class PaymentsViewModel(
    private val repository: PaymentsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoctorPaymentsUiState(isLoading = true))
    val uiState: StateFlow<DoctorPaymentsUiState> = _uiState

    init {
        loadReceiptsOLD()
    }
    fun loadReceipts(paymentId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.getPaymentsForDoctor(paymentId)) {
                is Result.Success -> {
                    val receipts = result.data
                    val items = receipts.map { it.toHistoryItem() }
                    _uiState.value = DoctorPaymentsUiState(
                        isLoading = false,
                        receipts = items,
                        summary = buildSummary(receipts)
                    )
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error cargando pagos"
                        )
                    }
                }
            }
        }
    }
    fun loadReceiptsOLD() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.getReceipts()) {
                is Result.Success -> {
                    val receipts = result.data
                    val items = receipts.map { it.toHistoryItem() }
                    _uiState.value = DoctorPaymentsUiState(
                        isLoading = false,
                        receipts = items,
                        summary = buildSummary(receipts)
                    )
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error cargando pagos"
                        )
                    }
                }
            }
        }
    }

    fun selectResident(residentId: Long?) {
        _uiState.update { it.copy(selectedResidentId = residentId) }
    }

    private fun buildSummary(receipts: List<ReceiptResponse>): PaymentsSummary {
        val toCollect = receipts
            .filter { !it.status }
            .sumOf { it.totalAmount - it.amountPaid }

        val collected = receipts
            .filter { it.status }
            .sumOf { it.amountPaid }

        val pendingCount = receipts.count { !it.status }

        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        return PaymentsSummary(
            totalToCollectLabel = currencyFormatter.format(toCollect),
            totalCollectedLabel = currencyFormatter.format(collected),
            pendingCount = pendingCount
        )
    }

    private fun ReceiptResponse.toHistoryItem(): PaymentHistoryItem {
        fun String.toDateLabel(): String =
            if (length >= 10) substring(0, 10) else this

        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        return PaymentHistoryItem(
            receiptId = receiptId,
            residentId = residentId,
            residentLabel = "Resident #$residentId",
            statusLabel = if (status) "Paid" else "Pending",
            issueDateLabel = issueDate.toDateLabel(),
            dueDateLabel = dueDate.toDateLabel(),
            totalAmountLabel = currencyFormatter.format(totalAmount),
            amountPaidLabel = currencyFormatter.format(amountPaid),
            paymentMethodLabel = when (paymentMethod) {
                1 -> "Cash"
                2 -> "Card"
                3 -> "Transfer"
                else -> "Other"
            }
        )
    }

    class Factory(
        private val repository: PaymentsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PaymentsViewModel::class.java)) {
                return PaymentsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
