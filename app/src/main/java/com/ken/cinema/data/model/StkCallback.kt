package com.ken.cinema.data.model

data class StkCallback(
    val CallbackMetadata: CallbackMetadata,
    val CheckoutRequestID: String,
    val MerchantRequestID: String,
    val ResultCode: Int,
    val ResultDescription: String
)