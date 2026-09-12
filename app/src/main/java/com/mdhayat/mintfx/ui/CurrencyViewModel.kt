package com.mdhayat.mintfx.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mdhayat.mintfx.data.local.CurrencyPreferences
import com.mdhayat.mintfx.data.remote.CurrencyApiService
import com.mdhayat.mintfx.data.repository.CurrencyRepository
import com.mdhayat.mintfx.domain.CurrencyCalculator
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    fun refreshRates() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getRates()
                .onSuccess { snapshot ->
                    rates = snapshot.rates
                    val currencies = rates.keys.sorted()
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
                        .baseUrl("https://api.frankfurter.app/")
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
