package com.mdhayat.mintfx.data.remote

data class FrankfurterRatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
