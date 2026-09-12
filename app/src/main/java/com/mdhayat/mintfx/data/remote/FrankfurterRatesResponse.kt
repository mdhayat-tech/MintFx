package com.mdhayat.mintfx.data.remote

data class FrankfurterRatesResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
