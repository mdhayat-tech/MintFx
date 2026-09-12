package com.mdhayat.mintfx.domain

import java.util.Locale

object CurrencyCalculator {
    fun convert(amountInput: String, rate: Double): String {
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        return String.format(Locale.US, "%.2f", amount * rate)
    }
}
