package com.mdhayat.mintfx

import com.mdhayat.mintfx.domain.CurrencyCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyCalculatorTest {

    @Test
    fun `convert returns formatted amount`() {
        val converted = CurrencyCalculator.convert("10.5", 1.25)
        assertEquals("13.12", converted)
    }

    @Test
    fun `convert handles invalid input safely`() {
        val converted = CurrencyCalculator.convert("abc", 2.0)
        assertEquals("0.00", converted)
    }
}
