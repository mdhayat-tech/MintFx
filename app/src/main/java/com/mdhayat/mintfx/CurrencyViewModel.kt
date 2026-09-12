package com.mdhayat.mintfx

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val preferredCurrencies = listOf(
    "USD", "BDT", "EUR", "GBP", "INR", "JPY", "AUD", "CAD", "CHF", "CNY", "SGD", "AED", "SAR", "ZAR"
)

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyUiState(isLoading = true))
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()
    private var rates: Map<String, Double> = emptyMap()

    init {
        refreshRates()
    }

    fun onAmountChange(value: String) {
        if (value.isEmpty() || value.matches(Regex("^\\d*(\\.\\d*)?$"))) {
            _uiState.update { it.copy(amountInput = value) }
            updateConvertedAmount()
        }
    }

    fun onSourceCurrencyChange(value: String) {
        _uiState.update { it.copy(sourceCurrency = value) }
        updateConvertedAmount()
    }

    fun onTargetCurrencyChange(value: String) {
        _uiState.update { it.copy(targetCurrency = value) }
        updateConvertedAmount()
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                sourceCurrency = it.targetCurrency,
                targetCurrency = it.sourceCurrency
            )
        }
        updateConvertedAmount()
    }

    fun refreshRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getRates()
                .onSuccess { snapshot ->
                    rates = snapshot.rates
                    val currencies = preferredCurrencies.filter { it in rates } +
                        rates.keys.filterNot { it in preferredCurrencies }.sorted()
                    _uiState.update { state ->
                        state.copy(
                            availableCurrencies = currencies,
                            isLoading = false,
                            isUsingCachedRates = snapshot.isCached,
                            errorMessage = null,
                            sourceCurrency = if (state.sourceCurrency in rates) state.sourceCurrency else currencies.first(),
                            targetCurrency = if (state.targetCurrency in rates) state.targetCurrency else currencies.getOrElse(1) { currencies.first() }
                        )
                    }
                    updateConvertedAmount()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "No rates are available yet."
                        )
                    }
                }
        }
    }

    private fun updateConvertedAmount() {
        val state = _uiState.value
        val amount = state.amountInput.toDoubleOrNull() ?: 0.0
        val sourceRate = rates[state.sourceCurrency] ?: 0.0
        val targetRate = rates[state.targetCurrency] ?: 0.0
        _uiState.update {
            it.copy(
                convertedAmount = CurrencyCalculator.convert(amount, sourceRate, targetRate)
            )
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://open.er-api.com/v6/latest/")
                        .addConverterFactory(
                            GsonConverterFactory.create(GsonBuilder().create())
                        )
                        .build()
                    val api = retrofit.create(CurrencyApiService::class.java)
                    return CurrencyViewModel(
                        CurrencyRepository(api, CurrencyPreferences(context))
                    ) as T
                }
            }
    }
}
