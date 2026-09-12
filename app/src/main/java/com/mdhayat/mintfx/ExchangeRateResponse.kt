package com.mdhayat.mintfx

data class ExchangeRateResponse(
    val base_code: String,
    val rates: Map<String, Double>
)
