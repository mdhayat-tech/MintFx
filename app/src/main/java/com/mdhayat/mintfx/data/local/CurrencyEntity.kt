package com.mdhayat.mintfx.data.local

import androidx.room.Entity

@Entity(
    tableName = "currency_rates",
    primaryKeys = ["baseCurrency", "currencyCode"]
)
data class CurrencyEntity(
    val baseCurrency: String,
    val currencyCode: String,
    val rate: Double,
    val updatedAt: Long
)
