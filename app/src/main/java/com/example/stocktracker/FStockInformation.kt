package com.example.stocktracker

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FStockInformation(
    val qrcode:String?=null,
    val stockName: String? = null,
    val stockQuantity: Int? = null
)
