package com.mdhayat.mintfx.data.repository

import com.mdhayat.mintfx.data.local.CurrencyDao
import com.mdhayat.mintfx.data.local.CurrencyEntity
import com.mdhayat.mintfx.data.remote.CurrencyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val apiService: CurrencyApiService,
    private val currencyDao: CurrencyDao
) {
    suspend fun getRates(baseCurrency: String): RateLoadResult = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestRates(baseCurrency)
            val mergedRates = response.rates.toMutableMap().apply { put(response.base, 1.0) }
            val timestamp = System.currentTimeMillis()
            val entities = mergedRates.map { (code, rate) ->
                CurrencyEntity(
                    baseCurrency = response.base,
                    currencyCode = code,
                    rate = rate,
                    updatedAt = timestamp
                )
            }

            currencyDao.deleteRatesForBase(response.base)
            currencyDao.insertRates(entities)

            RateLoadResult(
                rates = mergedRates,
                usingCachedRates = false,
                errorMessage = null
            )
        } catch (exception: Exception) {
            val cachedRates = currencyDao.getRatesForBase(baseCurrency)
            if (cachedRates.isNotEmpty()) {
                RateLoadResult(
                    rates = cachedRates.associate { it.currencyCode to it.rate },
                    usingCachedRates = true,
                    errorMessage = "Using cached rates. Latest update could not be fetched."
                )
            } else {
                RateLoadResult(
                    rates = emptyMap(),
                    usingCachedRates = true,
                    errorMessage = "No internet and no cached rates available yet."
                )
            }
        }
    }
}

data class RateLoadResult(
    val rates: Map<String, Double>,
    val usingCachedRates: Boolean,
    val errorMessage: String?
)
