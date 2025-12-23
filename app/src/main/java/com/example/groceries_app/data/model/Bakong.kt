package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

// Request models
data class BakongQrRequest(
    @SerializedName("amount") val amount: String
)

data class BakongQrImageRequest(
    @SerializedName("qr") val qr: String,
    @SerializedName("md5") val md5: String
)

data class BakongTransactionCheckRequest(
    @SerializedName("md5") val md5: String
)

// Response models
data class BakongQrResponse(
    @SerializedName("data") val data: BakongQrData,
    @SerializedName("khqrstatus") val khqrstatus: KhqrStatus
)

data class BakongQrData(
    @SerializedName("qr") val qr: String,
    @SerializedName("md5") val md5: String
)

data class KhqrStatus(
    @SerializedName("code") val code: Int,
    @SerializedName("errorCode") val errorCode: String?,
    @SerializedName("message") val message: String?
)

data class BakongTransactionResponse(
    @SerializedName("responseCode") val responseCode: Int,
    @SerializedName("responseMessage") val responseMessage: String,
    @SerializedName("errorCode") val errorCode: Int?,
    @SerializedName("data") val data: BakongTransactionData?
)

data class BakongTransactionData(
    @SerializedName("hash") val hash: String,
    @SerializedName("fromAccountId") val fromAccountId: String,
    @SerializedName("toAccountId") val toAccountId: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String?,
    @SerializedName("createdDateMs") val createdDateMs: Long,
    @SerializedName("acknowledgedDateMs") val acknowledgedDateMs: Long,
    @SerializedName("trackingStatus") val trackingStatus: String?,
    @SerializedName("receiverBank") val receiverBank: String?,
    @SerializedName("receiverBankAccount") val receiverBankAccount: String?,
    @SerializedName("instructionRef") val instructionRef: String?,
    @SerializedName("externalRef") val externalRef: String?
)

