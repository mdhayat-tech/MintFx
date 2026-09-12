package com.mdhayat.mintfx.ui

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
