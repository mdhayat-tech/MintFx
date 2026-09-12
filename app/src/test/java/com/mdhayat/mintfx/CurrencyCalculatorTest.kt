package com.mdhayat.mintfx

import com.mdhayat.mintfx.domain.CurrencyCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyCalculatorTest {
    @Test
    fun convertsBetweenRatesRelativeToTheSameBase() {
        assertEquals("15.00", CurrencyCalculator.convert(10.0, 0.8, 1.2))
    }

    @Test
    fun invalidRateReturnsZero() {
        assertEquals("0.00", CurrencyCalculator.convert(10.0, 0.0, 1.2))
    }
}
