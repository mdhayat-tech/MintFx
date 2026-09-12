package com.mdhayat.mintfx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdhayat.mintfx.data.repository.CurrencyRepository
import com.mdhayat.mintfx.domain.CurrencyCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    private var latestRates: Map<String, Double> = emptyMap()

    init {
        refreshRates()
    }

    fun onAmountChange(newValue: String) {
        if (newValue.isEmpty() || newValue.matches(Regex("\\d*\\.?\\d*"))) {
            _uiState.update { it.copy(amountInput = newValue) }
            updateConvertedAmount()
        }
    }

    fun onSourceCurrencyChange(currency: String) {
        _uiState.update { it.copy(sourceCurrency = currency) }
        refreshRates()
    }

    fun onTargetCurrencyChange(currency: String) {
        _uiState.update { it.copy(targetCurrency = currency) }
        updateConvertedAmount()
    }

    fun refreshRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val currentBase = _uiState.value.sourceCurrency
            val result = repository.getRates(currentBase)

            latestRates = result.rates
            val available = if (result.rates.isEmpty()) {
                _uiState.value.availableCurrencies
            } else {
                result.rates.keys.sorted()
            }

            val newTarget = _uiState.value.targetCurrency.takeIf { available.contains(it) }
                ?: available.firstOrNull { it != currentBase }
                ?: currentBase

            _uiState.update {
                it.copy(
                    availableCurrencies = available,
                    targetCurrency = newTarget,
                    isLoading = false,
                    isUsingCachedRates = result.usingCachedRates,
                    errorMessage = result.errorMessage
                )
            }

            updateConvertedAmount()
        }
    }

    private fun updateConvertedAmount() {
        val state = _uiState.value
        val rate = latestRates[state.targetCurrency] ?: 0.0
        _uiState.update {
            it.copy(convertedAmount = CurrencyCalculator.convert(state.amountInput, rate))
        }
    }
}
