package com.mdhayat.mintfx

import java.util.Locale

object CurrencyCalculator {
    fun convert(amount: Double, sourceRate: Double, targetRate: Double): String {
        if (sourceRate <= 0.0 || targetRate <= 0.0) return "0.00"
        return String.format(Locale.US, "%.2f", amount * targetRate / sourceRate)
    }
}
