package com.mdhayat.mintfx

data class CurrencyUiState(
    val amountInput: String = "1",
    val sourceCurrency: String = "USD",
    val targetCurrency: String = "EUR",
    val availableCurrencies: List<String> = listOf("USD", "EUR"),
    val convertedAmount: String = "0.00",
    val isLoading: Boolean = false,
    val isUsingCachedRates: Boolean = false,
    val errorMessage: String? = null
)

fun currencyLabel(code: String): String = when (code) {
    "AED" -> "AED-(United Arab Emirates)"
    "AUD" -> "AUD-(Australia)"
    "BDT" -> "BDT-(Bangladesh)"
    "CAD" -> "CAD-(Canada)"
    "CHF" -> "CHF-(Switzerland)"
    "CNY" -> "CNY-(China)"
    "EUR" -> "EUR-(European Union)"
    "GBP" -> "GBP-(United Kingdom)"
    "INR" -> "INR-(India)"
    "JPY" -> "JPY-(Japan)"
    "SAR" -> "SAR-(Saudi Arabia)"
    "SGD" -> "SGD-(Singapore)"
    "USD" -> "USD-(United States)"
    "ZAR" -> "ZAR-(South Africa)"
    else -> code
}
