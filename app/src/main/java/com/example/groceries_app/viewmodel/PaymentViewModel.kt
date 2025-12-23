package com.example.groceries_app.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.api.RetrofitClient
import com.example.groceries_app.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class QrGenerated(val qrData: BakongQrData, val qrImage: Bitmap?) : PaymentState()
    data class PaymentSuccess(val transactionData: BakongTransactionData) : PaymentState()
    data class PaymentFailed(val message: String) : PaymentState()
    data class Error(val message: String) : PaymentState()
}

class PaymentViewModel : ViewModel() {
    private val api = RetrofitClient.nectarApi

    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val paymentState: StateFlow<PaymentState> = _paymentState.asStateFlow()

    private val _timeRemaining = MutableStateFlow(180) // 3 minutes in seconds
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()

    private var pollingJob: Job? = null
    private var timerJob: Job? = null
    private var currentMd5: String? = null

    fun generateQrCode(amount: Double) {
        viewModelScope.launch {
            try {
                _paymentState.value = PaymentState.Loading

                // Step 1: Generate QR string
                val qrRequest = BakongQrRequest(amount = amount.toString())
                val qrResponse = api.generateBakongQr(qrRequest)

                if (qrResponse.isSuccessful && qrResponse.body() != null) {
                    val qrData = qrResponse.body()!!.data
                    currentMd5 = qrData.md5

                    // Step 2: Get QR image
                    val imageRequest = BakongQrImageRequest(
                        qr = qrData.qr,
                        md5 = qrData.md5
                    )
                    val imageResponse = api.getBakongQrImage(imageRequest)

                    val qrBitmap = if (imageResponse.isSuccessful && imageResponse.body() != null) {
                        val imageBytes = imageResponse.body()!!.bytes()
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    } else {
                        null
                    }

                    _paymentState.value = PaymentState.QrGenerated(qrData, qrBitmap)

                    // Step 3: Start polling for transaction status
                    startTransactionPolling(qrData.md5)

                } else {
                    _paymentState.value = PaymentState.Error("Failed to generate QR code: ${qrResponse.message()}")
                }

            } catch (e: Exception) {
                _paymentState.value = PaymentState.Error("Error: ${e.message ?: "Unknown error"}")
            }
        }
    }

    private fun startTransactionPolling(md5: String) {
        // Cancel any existing polling
        pollingJob?.cancel()
        timerJob?.cancel()

        // Reset timer
        _timeRemaining.value = 180

        // Start countdown timer
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value > 0) {
                delay(1000) // 1 second
                _timeRemaining.value -= 1
            }
            // Time expired
            stopPolling()
            if (_paymentState.value is PaymentState.QrGenerated) {
                _paymentState.value = PaymentState.PaymentFailed("Payment time expired. Please try again.")
            }
        }

        // Start polling every 3 seconds
        pollingJob = viewModelScope.launch {
            while (true) {
                try {
                    val checkRequest = BakongTransactionCheckRequest(md5 = md5)
                    val response = api.checkBakongTransaction(checkRequest)

                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        when (result.responseCode) {
                            0 -> {
                                // Success - payment received
                                if (result.data != null) {
                                    _paymentState.value = PaymentState.PaymentSuccess(result.data)
                                    stopPolling()
                                    break
                                }
                            }
                            1 -> {
                                // Transaction not found yet - continue polling
                                // Do nothing, keep polling
                            }
                            else -> {
                                // Other error
                                _paymentState.value = PaymentState.PaymentFailed(result.responseMessage)
                                stopPolling()
                                break
                            }
                        }
                    }

                    // Wait 3 seconds before next poll
                    delay(3000)

                } catch (e: Exception) {
                    // Log error but continue polling
                    delay(3000)
                }
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
        timerJob?.cancel()
        timerJob = null
    }

    fun resetPayment() {
        stopPolling()
        _paymentState.value = PaymentState.Idle
        _timeRemaining.value = 180
        currentMd5 = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}

